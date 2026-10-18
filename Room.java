public class Room {
    private final int number;
    private final RoomType type;

    public Room(int number, RoomType type) {
        this.number = number;
        this.type = type;
    }

    public int getNumber() { return number; }
    public RoomType getType() { return type; }

    @Override
    public String toString() {
        return String.format("Room %d | %-8s | Rs.%.0f/night", number, type, type.getPricePerNight());
    }
}
