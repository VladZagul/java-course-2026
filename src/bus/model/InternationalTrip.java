package bus.model;

import java.time.LocalDateTime;

public final class InternationalTrip extends IntercityTrip {
    public static final double DEFAULT_BORDER_FEE = 150;

    private final String country;
    private final double borderFee;

    public InternationalTrip(long id, String route, LocalDateTime departureTime, double price, boolean wifi,
            String country, double borderFee) {
        super(id, route, departureTime, price, wifi);
        this.country = country;
        this.borderFee = borderFee;
        trace("3. InternationalTrip: country = " + country + ", borderFee = " + borderFee);
    }

    public InternationalTrip(long id, String route, LocalDateTime departureTime, double price, String country) {
        this(id, route, departureTime, price, true, country, DEFAULT_BORDER_FEE);
    }

    public String getCountry() {
        return country;
    }

    public double getBorderFee() {
        return borderFee;
    }

    @Override
    public double getExtraFee() {
        return borderFee;
    }

    @Override
    public boolean requiresPassport() {
        return true;
    }

    @Override
    public String getTypeName() {
        return "Міжнародний";
    }

    @Override
    public int getBaggageAllowanceKg() {
        return 23;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", країна: %s, збір за кордон %.2f грн", country, borderFee);
    }
}
