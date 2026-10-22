package bus.fare;

import bus.model.Booking;

public class StudentFare extends AbstractFare {

    public StudentFare() {
        super("Студентський", 30);
    }

    @Override
    protected int countDiscountedSeats(Booking booking) {
        return 1;
    }

    @Override
    public boolean requiresDocument() {
        return true;
    }
}
