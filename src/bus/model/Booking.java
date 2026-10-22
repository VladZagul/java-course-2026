package bus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bus.exception.InvalidQuantityException;
import bus.observer.BookingObserver;

public class Booking implements Payable, Comparable<Booking> {
    private String passengerName;
    private Trip trip;
    private int seats;
    private BookingStatus status = BookingStatus.NEW;
    private final List<BookingObserver> observers = new ArrayList<>();

    public Booking(String passengerName, Trip trip, int seats) throws InvalidQuantityException {
        if (seats <= 0) {
            throw new InvalidQuantityException(
                "Кількість місць має бути додатньою, отримано: " + seats, seats
            );
        }

        this.passengerName = passengerName;
        this.trip = trip;
        this.seats = seats;
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
        return seats * trip.getFinalPrice();
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Trip getTrip() {
        return trip;
    }

    public String getRoute() {
        return trip.getRoute();
    }

    public int getSeats() {
        return seats;
    }

    public double getPricePerSeat() {
        return trip.getFinalPrice();
    }

    @Override
    public int compareTo(Booking other) {
        return Double.compare(getTotalCost(), other.getTotalCost());
    }

    @Override
    public String toString() {
        return String.format(
            "Пасажир: %-15s Рейс: %-24s Місць: %-3d Ціна: %-8.2f Сума: %-9.2f Статус: %s",
            passengerName, trip.getRoute(), seats, trip.getFinalPrice(), getTotalCost(), status.getTitle()
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
                && passengerName.equals(other.passengerName)
                && trip.equals(other.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerName, trip, seats);
    }
}
