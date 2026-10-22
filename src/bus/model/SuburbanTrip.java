package bus.model;

import java.time.LocalDateTime;
import java.util.Objects;

import bus.exception.InvalidPriceException;

public final class SuburbanTrip extends Trip {
    private final int stopsCount;

    public SuburbanTrip(String route, LocalDateTime departureTime, double price, int stopsCount)
            throws InvalidPriceException {
        super(route, departureTime, price);
        this.stopsCount = stopsCount;
        trace("2. SuburbanTrip: stopsCount = " + stopsCount);
    }

    public int getStopsCount() {
        return stopsCount;
    }

    @Override
    public String getTypeName() {
        return "Приміський";
    }

    @Override
    public int getBaggageAllowanceKg() {
        return 10;
    }

    @Override
    public String toString() {
        return super.toString() + ", зупинок: " + stopsCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        SuburbanTrip other = (SuburbanTrip) obj;
        return stopsCount == other.stopsCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), stopsCount);
    }
}
