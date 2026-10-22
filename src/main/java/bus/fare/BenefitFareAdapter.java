package bus.fare;

import bus.fare.external.StateBenefitSystem;
import bus.model.Booking;

public class BenefitFareAdapter implements Fare {
    private final StateBenefitSystem benefitSystem;

    public BenefitFareAdapter(StateBenefitSystem benefitSystem) {
        this.benefitSystem = benefitSystem;
    }

    @Override
    public String getName() {
        return benefitSystem.getProgramName();
    }

    @Override
    public double calculateCost(Booking booking) {
        long priceKopecks = Math.round(booking.getPricePerSeat() * 100);
        long paymentKopecks = benefitSystem.calculatePaymentKopecks(booking.getSeats(), priceKopecks);
        return paymentKopecks / 100.0;
    }

    @Override
    public boolean requiresDocument() {
        return benefitSystem.isCertificateRequired();
    }
}
