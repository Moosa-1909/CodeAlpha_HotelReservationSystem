import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** File I/O persistence for bookings (CSV). */
public class BookingStore {
    private final Path file;

    public BookingStore(String fileName) { this.file = Paths.get(fileName); }

    public List<Booking> load() {
        List<Booking> list = new ArrayList<>();
        if (!Files.exists(file)) return list;
        try {
            for (String line : Files.readAllLines(file)) {
                if (!line.isBlank()) list.add(Booking.fromCsv(line));
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Warning: could not read bookings file: " + e.getMessage());
        }
        return list;
    }

    public void save(List<Booking> bookings) {
        List<String> lines = new ArrayList<>();
        for (Booking b : bookings) lines.add(b.toCsv());
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            System.out.println("Error: could not save bookings: " + e.getMessage());
        }
    }
}
