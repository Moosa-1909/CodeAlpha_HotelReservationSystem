public enum RoomType {
    STANDARD(2000), DELUXE(3500), SUITE(6000);

    private final double pricePerNight;

    RoomType(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public double getPricePerNight() { return pricePerNight; }
}
