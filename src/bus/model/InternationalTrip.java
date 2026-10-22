package bus.model;

import java.time.LocalDateTime;
import java.util.Objects;

import bus.exception.InvalidPriceException;

public final class InternationalTrip extends IntercityTrip {
    public static final double DEFAULT_BORDER_FEE = 150;

    private final String country;
    private final double borderFee;

    public InternationalTrip(String route, LocalDateTime departureTime, double price, boolean wifi,
            String country, double borderFee) throws InvalidPriceException {
        super(route, departureTime, price, wifi);
        this.country = country;
        this.borderFee = borderFee;
        trace("3. InternationalTrip: country = " + country + ", borderFee = " + borderFee);
    }

    public InternationalTrip(String route, LocalDateTime departureTime, double price, String country)
            throws InvalidPriceException {
        this(route, departureTime, price, true, country, DEFAULT_BORDER_FEE);
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

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        InternationalTrip other = (InternationalTrip) obj;
        return Double.compare(borderFee, other.borderFee) == 0 && country.equals(other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), country, borderFee);
    }
}
