package bus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bus.exception.InvalidPriceException;
import bus.exception.InvalidQuantityException;
import bus.observer.BookingObserver;

public class Booking implements Payable, Comparable<Booking> {
    private String passengerName;
    private String route;
    private int seats;
    private double pricePerSeat;
    private BookingStatus status = BookingStatus.NEW;
    private final List<BookingObserver> observers = new ArrayList<>();

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

    public void addObserver(BookingObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(BookingObserver observer) {
        observers.remove(observer);
    }

    public void setStatus(BookingStatus newStatus) {
        if (newStatus == status) {
            return;
        }
        BookingStatus oldStatus = status;
        status = newStatus;
        notifyObservers(oldStatus, newStatus);
    }

    private void notifyObservers(BookingStatus oldStatus, BookingStatus newStatus) {
        for (BookingObserver observer : observers) {
            observer.onStatusChanged(this, oldStatus, newStatus);
        }
    }

    public BookingStatus getStatus() {
        return status;
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
            "Пасажир: %-15s Рейс: %-20s Місць: %-3d Ціна: %-8.2f Сума: %-9.2f Статус: %s",
            passengerName, route, seats, pricePerSeat, getTotalCost(), status.getTitle()
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
