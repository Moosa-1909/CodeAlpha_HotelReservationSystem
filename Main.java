import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static HotelService hotel;

    public static void main(String[] args) {
        hotel = new HotelService(new BookingStore("bookings.csv"));
        System.out.println("=== Hotel Reservation System ===");
        while (true) {
            System.out.println("\n1. Search available rooms\n2. Book a room\n3. Cancel a booking"
                    + "\n4. View booking details\n5. View all bookings\n6. Exit");
            switch (ask("Choose: ")) {
                case "1": run(Main::search); break;
                case "2": run(Main::book); break;
                case "3": run(Main::cancel); break;
                case "4": run(Main::view); break;
                case "5": hotel.allBookings().forEach(System.out::println); break;
                case "6": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void run(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException | IllegalStateException | DateTimeParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String ask(String prompt) { System.out.print(prompt); return sc.nextLine().trim(); }

    private static LocalDate date(String prompt) { return LocalDate.parse(ask(prompt + " (YYYY-MM-DD): ")); }

    private static RoomType askType() {
        String t = ask("Category (STANDARD/DELUXE/SUITE, blank = any): ").toUpperCase();
        return t.isEmpty() ? null : RoomType.valueOf(t);
    }

    private static void search() {
        RoomType type = askType();
        List<Room> rooms = hotel.search(type, date("Check-in"), date("Check-out"));
        if (rooms.isEmpty()) System.out.println("No rooms available.");
        else rooms.forEach(System.out::println);
    }

    private static void book() {
        String guest = ask("Guest name: ");
        int room = Integer.parseInt(ask("Room number: "));
        LocalDate in = date("Check-in"), out = date("Check-out");
        PaymentSimulator.Method m = PaymentSimulator.Method.valueOf(ask("Payment (CARD/UPI/CASH): ").toUpperCase());
        String detail = m == PaymentSimulator.Method.CARD ? ask("Last 4 digits of card: ")
                : m == PaymentSimulator.Method.UPI ? ask("UPI id: ") : "";
        Booking b = hotel.book(guest, room, in, out, m, detail);
        System.out.println("Booking confirmed!\n" + b);
    }

    private static void cancel() {
        double refund = hotel.cancel(ask("Booking id: "));
        System.out.printf("Cancelled. Refund: Rs.%.0f%n", refund);
    }

    private static void view() {
        String id = ask("Booking id: ");
        System.out.println(hotel.find(id).map(Booking::toString).orElse("Not found."));
    }
}
