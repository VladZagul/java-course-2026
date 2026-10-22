public abstract class AbstractFare implements Fare {
    private final String name;
    private final int discountPercent;

    protected AbstractFare(String name, int discountPercent) {
        this.name = name;
        this.discountPercent = discountPercent;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public double calculateCost(Booking booking) {
        int discountedSeats = Math.min(countDiscountedSeats(booking), booking.getSeats());
        double discount = discountedSeats * booking.getPricePerSeat() * discountPercent / 100.0;
        double cost = booking.getTotalCost() - discount;
        return Math.round(cost * 100) / 100.0;
    }

    protected abstract int countDiscountedSeats(Booking booking);
}
