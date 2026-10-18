import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class HotelService {
    private final List<Room> rooms = new ArrayList<>();
    private final List<Booking> bookings;
    private final BookingStore store;

    public HotelService(BookingStore store) {
        this.store = store;
        this.bookings = store.load();
        for (int i = 101; i <= 104; i++) rooms.add(new Room(i, RoomType.STANDARD));
        for (int i = 201; i <= 203; i++) rooms.add(new Room(i, RoomType.DELUXE));
        for (int i = 301; i <= 302; i++) rooms.add(new Room(i, RoomType.SUITE));
    }

    private void validateDates(LocalDate in, LocalDate out) {
        if (in.isBefore(LocalDate.now())) throw new IllegalArgumentException("Check-in cannot be in the past");
        if (!out.isAfter(in)) throw new IllegalArgumentException("Check-out must be after check-in");
    }

    public boolean isAvailable(int roomNumber, LocalDate in, LocalDate out) {
        return bookings.stream()
                .noneMatch(b -> b.isActive() && b.getRoomNumber() == roomNumber && b.overlaps(in, out));
    }

    /** type == null means any category. */
    public List<Room> search(RoomType type, LocalDate in, LocalDate out) {
        validateDates(in, out);
        return rooms.stream()
                .filter(r -> type == null || r.getType() == type)
                .filter(r -> isAvailable(r.getNumber(), in, out))
                .collect(Collectors.toList());
    }

    public Booking book(String guest, int roomNumber, LocalDate in, LocalDate out,
                        PaymentSimulator.Method method, String paymentDetail) {
        validateDates(in, out);
        if (guest == null || guest.isBlank()) throw new IllegalArgumentException("Guest name required");
        Room room = rooms.stream().filter(r -> r.getNumber() == roomNumber).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such room: " + roomNumber));
        if (!isAvailable(roomNumber, in, out)) throw new IllegalStateException("Room not available for those dates");

        long nights = ChronoUnit.DAYS.between(in, out);
        double amount = nights * room.getType().getPricePerNight();
        String txn = PaymentSimulator.pay(amount, method, paymentDetail);

        String id = "BK" + (1000 + bookings.size() + 1);
        Booking booking = new Booking(id, guest.trim(), roomNumber, in, out, amount, txn, Booking.Status.CONFIRMED);
        bookings.add(booking);
        store.save(bookings);
        return booking;
    }

    /** Returns refund amount. Full refund if 2+ days before check-in, else 50%. */
    public double cancel(String bookingId) {
        Booking b = find(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (!b.isActive()) throw new IllegalStateException("Booking already cancelled");
        long daysToCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), b.getCheckIn());
        double refund = daysToCheckIn >= 2 ? b.getAmount() : b.getAmount() * 0.5;
        b.cancel();
        store.save(bookings);
        return refund;
    }

    public Optional<Booking> find(String id) {
        return bookings.stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst();
    }

    public List<Booking> bookingsFor(String guest) {
        return bookings.stream().filter(b -> b.getGuestName().equalsIgnoreCase(guest.trim()))
                .collect(Collectors.toList());
    }

    public List<Booking> allBookings() { return Collections.unmodifiableList(bookings); }
}
