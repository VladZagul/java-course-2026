package bus.model;

import java.time.LocalDateTime;

public sealed class IntercityTrip extends Trip permits InternationalTrip {
    private final boolean wifi;

    public IntercityTrip(long id, String route, LocalDateTime departureTime, double price, boolean wifi) {
        super(id, route, departureTime, price);
        this.wifi = wifi;
        trace("2. IntercityTrip: wifi = " + wifi);
    }

    public IntercityTrip(long id, String route, LocalDateTime departureTime, double price) {
        this(id, route, departureTime, price, true);
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
}
