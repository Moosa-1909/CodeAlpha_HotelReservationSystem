# 🏨 Hotel Reservation System

A console-based hotel reservation system built in **Java** using object-oriented design and file-based persistence. Search rooms by category and dates, book with a simulated payment, cancel with an automatic refund policy, and keep all bookings saved between runs.

> Built as **Task 4** of the **CodeAlpha Java Programming Internship**.

---

## ✨ Features

| Feature | Details |
|---|---|
| **Room categories** | Standard, Deluxe and Suite, each with its own nightly price |
| **Search** | Find available rooms by category and check-in / check-out dates |
| **Booking** | Reserve a room for a date range; total is calculated from nights stayed |
| **Double-booking protection** | Overlapping stays on the same room are rejected |
| **Payment simulation** | Card (last 4 digits), UPI ID or Cash, with input validation and a generated transaction ID |
| **Cancellation** | Cancel by booking ID with an automatic refund calculation |
| **Booking details** | View a single booking or the full booking list |
| **Persistence** | Bookings are saved to `bookings.csv` and reloaded on startup |

### Room inventory and pricing

| Category | Rooms | Price per night |
|---|---|---|
| Standard | 101 – 104 | ₹2,000 |
| Deluxe | 201 – 203 | ₹3,500 |
| Suite | 301 – 302 | ₹6,000 |

### Cancellation policy

- Cancelled **2 or more days** before check-in → **100% refund**
- Cancelled **less than 2 days** before check-in → **50% refund**

---

## 🧱 Project Structure

```
.
├── Main.java              # Console menu and user interaction
├── HotelService.java      # Business logic: search, book, cancel
├── Room.java              # Room model
├── RoomType.java          # Enum of categories with pricing
├── Booking.java           # Booking model + CSV (de)serialization
├── BookingStore.java      # File I/O persistence layer
├── PaymentSimulator.java  # Simulated payment gateway
└── README.md
```

### Design overview

- **Separation of concerns:** the UI (`Main`), business rules (`HotelService`), storage (`BookingStore`) and domain models are kept in separate classes.
- **Date overlap check:** each stay is treated as a half-open interval `[checkIn, checkOut)`. Two bookings clash when `newIn < existingOut && existingIn < newOut`, so back-to-back stays (one guest checks out the day another checks in) are allowed.
- **Validation:** past dates, invalid ranges, unknown rooms, malformed card/UPI details and repeated cancellations are all handled with clear error messages instead of crashes.
- **No external dependencies:** just the Java standard library (`java.time`, `java.nio.file`, `java.util`).

---

## 🚀 Getting Started

### Requirements

- Java **11 or higher** (JDK)

### Run from the command line

```bash
git clone https://github.com/Moosa-1909/CodeAlpha_HotelReservationSystem.git
cd CodeAlpha_HotelReservationSystem
javac *.java
java Main
```

### Run in IntelliJ IDEA

1. Open the project folder.
2. Open `Main.java`.
3. Click the ▶ **Run** icon next to `public static void main`.

`bookings.csv` is created automatically in the working directory after your first booking.

---

## 🖥️ Sample Session

```
=== Hotel Reservation System ===

1. Search available rooms
2. Book a room
3. Cancel a booking
4. View booking details
5. View all bookings
6. Exit
Choose: 2
Guest name: Asha
Room number: 101
Check-in (YYYY-MM-DD): 2026-11-01
Check-out (YYYY-MM-DD): 2026-11-04
Payment (CARD/UPI/CASH): UPI
UPI id: asha@okbank
Booking confirmed!
[BK1001] Asha | Room 101 | 2026-11-01 -> 2026-11-04 | Rs.6000 | CONFIRMED | Txn: TXN-579E9DF7
```

Trying to book room 101 again for overlapping dates:

```
Error: Room not available for those dates
```

Cancelling:

```
Choose: 3
Booking id: BK1001
Cancelled. Refund: Rs.6000
```

### Data format (`bookings.csv`)

```
BK1001,Asha,101,2026-11-01,2026-11-04,6000.0,TXN-579E9DF7,CANCELLED
```

---

## 🔮 Possible Improvements

- Replace CSV storage with a database (SQLite / MySQL via JDBC)
- Add a REST API layer (Spring Boot) on top of `HotelService`
- Thread-safe booking to handle concurrent requests
- Unit tests for the overlap logic and refund rules
- Admin features: add/remove rooms, revenue reports

---

## 👤 Author

**Moosa**
B.E. Computer Science, Muffakham Jah College of Engineering and Technology
GitHub: [@Moosa-1909](https://github.com/Moosa-1909)

---

*Created as part of the CodeAlpha Internship program.*
