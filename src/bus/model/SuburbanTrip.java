package bus.model;

import java.time.LocalDateTime;

public final class SuburbanTrip extends Trip {
    private final int stopsCount;

    public SuburbanTrip(long id, String route, LocalDateTime departureTime, double price, int stopsCount) {
        super(id, route, departureTime, price);
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
}
