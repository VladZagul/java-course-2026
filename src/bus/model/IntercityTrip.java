package bus.model;

import java.time.LocalDateTime;
import java.util.Objects;

import bus.exception.InvalidPriceException;

public sealed class IntercityTrip extends Trip permits InternationalTrip {
    private final boolean wifi;

    public IntercityTrip(String route, LocalDateTime departureTime, double price, boolean wifi)
            throws InvalidPriceException {
        super(route, departureTime, price);
        this.wifi = wifi;
        trace("2. IntercityTrip: wifi = " + wifi);
    }

    public IntercityTrip(String route, LocalDateTime departureTime, double price) throws InvalidPriceException {
        this(route, departureTime, price, true);
    }

    public boolean hasWifi() {
        return wifi;
    }

    @Override
    public String getTypeName() {
        return "Міжміський";
    }

    @Override
    public int getBaggageAllowanceKg() {
        return 20;
    }

    @Override
    public String toString() {
        return super.toString() + (wifi ? ", Wi-Fi" : ", без Wi-Fi");
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        IntercityTrip other = (IntercityTrip) obj;
        return wifi == other.wifi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), wifi);
    }
}
