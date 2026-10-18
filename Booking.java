import java.time.LocalDate;

public class Booking {
    public enum Status { CONFIRMED, CANCELLED }

    private final String id;
    private final String guestName;
    private final int roomNumber;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double amount;
    private final String paymentRef;
    private Status status;

    public Booking(String id, String guestName, int roomNumber, LocalDate checkIn,
                   LocalDate checkOut, double amount, String paymentRef, Status status) {
        this.id = id;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.paymentRef = paymentRef;
        this.status = status;
    }

    public String getId() { return id; }
    public String getGuestName() { return guestName; }
    public int getRoomNumber() { return roomNumber; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getAmount() { return amount; }
    public String getPaymentRef() { return paymentRef; }
    public Status getStatus() { return status; }
    public void cancel() { this.status = Status.CANCELLED; }
    public boolean isActive() { return status == Status.CONFIRMED; }

    /** Half-open interval overlap: [checkIn, checkOut) */
    public boolean overlaps(LocalDate in, LocalDate out) {
        return in.isBefore(checkOut) && checkIn.isBefore(out);
    }

    public String toCsv() {
        return String.join(",", id, guestName.replace(",", " "), String.valueOf(roomNumber),
                checkIn.toString(), checkOut.toString(), String.valueOf(amount), paymentRef, status.name());
    }

    public static Booking fromCsv(String line) {
        String[] p = line.split(",", -1);
        return new Booking(p[0], p[1], Integer.parseInt(p[2]), LocalDate.parse(p[3]),
                LocalDate.parse(p[4]), Double.parseDouble(p[5]), p[6], Status.valueOf(p[7]));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Room %d | %s -> %s | Rs.%.0f | %s | Txn: %s",
                id, guestName, roomNumber, checkIn, checkOut, amount, status, paymentRef);
    }
}
