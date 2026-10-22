public class ChildFare extends AbstractFare {

    public ChildFare() {
        super("Дитячий", 50);
    }

    @Override
    protected int countDiscountedSeats(Booking booking) {
        return booking.getSeats();
    }

    @Override
    public boolean requiresDocument() {
        return true;
    }
}
