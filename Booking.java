import java.util.Objects;

public class Booking implements Payable, Comparable<Booking> {
    private String passengerName;
    private String route;
    private int seats;
    private double pricePerSeat;

    public Booking(String passengerName, String route, int seats, double pricePerSeat)
            throws InvalidPriceException, InvalidQuantityException {
        if (seats <= 0) {
            throw new InvalidQuantityException(
                "Кількість місць має бути додатньою, отримано: " + seats, seats
            );
        }
        if (pricePerSeat <= 0) {
            throw new InvalidPriceException(
                "Ціна місця має бути додатньою, отримано: " + pricePerSeat, pricePerSeat
            );
        }

        this.passengerName = passengerName;
        this.route = route;
        this.seats = seats;
        this.pricePerSeat = pricePerSeat;
    }

    @Override
    public double getTotalCost() {
        return seats * pricePerSeat;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getRoute() {
        return route;
    }

    public int getSeats() {
        return seats;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    @Override
    public int compareTo(Booking other) {
        return Double.compare(getTotalCost(), other.getTotalCost());
    }

    @Override
    public String toString() {
        return String.format(
            "Пасажир: %-15s Рейс: %-20s Місць: %-3d Ціна: %-8.2f Сума: %.2f",
            passengerName, route, seats, pricePerSeat, getTotalCost()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Booking other = (Booking) obj;
        return seats == other.seats
                && Double.compare(pricePerSeat, other.pricePerSeat) == 0
                && passengerName.equals(other.passengerName)
                && route.equals(other.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerName, route, seats, pricePerSeat);
    }
}
