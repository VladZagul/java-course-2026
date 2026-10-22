public class GroupFare implements Fare {
    public static final int FREE_EVERY = 5;

    @Override
    public String getName() {
        return "Груповий";
    }

    @Override
    public double calculateCost(Booking booking) {
        int freeSeats = booking.getSeats() / FREE_EVERY;
        return (booking.getSeats() - freeSeats) * booking.getPricePerSeat();
    }
}
