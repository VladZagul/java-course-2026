package bus.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bus.exception.InvalidPriceException;

public abstract sealed class Trip extends BaseEntity permits SuburbanTrip, IntercityTrip {
    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static boolean traceConstructors = false;

    private final LocalDateTime departureTime;
    private final double price;

    protected Trip(long id, String route, LocalDateTime departureTime, double price) {
        super(id, route);
        if (price <= 0) {
            throw new InvalidPriceException("Ціна квитка має бути додатньою, отримано: " + price, price);
        }
        this.departureTime = departureTime;
        this.price = price;
        trace("1. Trip: id = " + id + ", route = " + route + ", price = " + price);
    }

    public static void setTraceConstructors(boolean enabled) {
        traceConstructors = enabled;
    }

    protected static void trace(String message) {
        if (traceConstructors) {
            System.out.println("   " + message);
        }
    }

    public String getRoute() {
        return getName();
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public final double getPrice() {
        return price;
    }

    public double getExtraFee() {
        return 0;
    }

    public final double getFinalPrice() {
        return price + getExtraFee();
    }

    public boolean requiresPassport() {
        return false;
    }

    public abstract String getTypeName();

    public abstract int getBaggageAllowanceKg();

    @Override
    public String toString() {
        return String.format("#%d %s рейс \"%s\", відправлення %s, ціна %.2f грн",
                getId(), getTypeName(), getName(), departureTime.format(DATE_TIME), price);
    }
}
