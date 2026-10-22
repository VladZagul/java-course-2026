package bus.fare.external;

public class StateBenefitSystem {

    public String getProgramName() {
        return "Державна пільга";
    }

    public long calculatePaymentKopecks(int tickets, long fullPriceKopecks) {
        return tickets * fullPriceKopecks * 20 / 100;
    }

    public boolean isCertificateRequired() {
        return true;
    }
}
