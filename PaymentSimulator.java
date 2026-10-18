import java.util.UUID;

/** Simulated payment gateway - no real money, just validation + a transaction id. */
public class PaymentSimulator {
    public enum Method { CARD, UPI, CASH }

    public static String pay(double amount, Method method, String detail) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
        if (method == Method.CARD && !detail.matches("\\d{4}")) {
            throw new IllegalArgumentException("Enter the last 4 digits of the card");
        }
        if (method == Method.UPI && !detail.matches("[\\w.\\-]+@[\\w]+")) {
            throw new IllegalArgumentException("Invalid UPI id (example: name@bank)");
        }
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
