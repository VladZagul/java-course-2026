package bus.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import bus.exception.InvalidPriceException;

public abstract sealed class Trip permits SuburbanTrip, IntercityTrip {
    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static boolean traceConstructors = false;

    private final String route;
    private final LocalDateTime departureTime;
    private final double price;

    protected Trip(String route, LocalDateTime departureTime, double price) throws InvalidPriceException {
        if (price <= 0) {
            throw new InvalidPriceException("Ціна квитка має бути додатньою, отримано: " + price, price);
        }
        this.route = route;
        this.departureTime = departureTime;
        this.price = price;
        trace("1. Trip: route = " + route + ", price = " + price);
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
        return route;
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
        return String.format("%s рейс \"%s\", відправлення %s, ціна %.2f грн",
                getTypeName(), route, departureTime.format(DATE_TIME), price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Trip other = (Trip) obj;
        return Double.compare(price, other.price) == 0
                && route.equals(other.route)
                && departureTime.equals(other.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, departureTime, price);
    }
}
