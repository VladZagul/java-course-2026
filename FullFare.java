public class FullFare extends AbstractFare {

    public FullFare() {
        super("Повний", 0);
    }

    @Override
    protected int countDiscountedSeats(Booking booking) {
        return 0;
    }
}
