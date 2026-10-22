package bus.model;

import java.util.ArrayList;
import java.util.List;

import bus.exception.InvalidQuantityException;
import bus.observer.BookingObserver;

public class Booking extends BaseEntity implements Payable, Comparable<Booking> {
    private final Trip trip;
    private final int seats;
    private BookingStatus status;
    private final List<BookingObserver> observers = new ArrayList<>();

    public Booking(long id, String passengerName, Trip trip, int seats, BookingStatus status) {
        super(id, passengerName);
        if (seats <= 0) {
            throw new InvalidQuantityException(
                "Кількість місць має бути додатньою, отримано: " + seats, seats
            );
        }
        this.trip = trip;
        this.seats = seats;
        this.status = status;
    }

    public Booking(long id, String passengerName, Trip trip, int seats) {
        this(id, passengerName, trip, seats, BookingStatus.NEW);
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
        return getName();
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
            "#%-3d Пасажир: %-15s Рейс: %-24s Місць: %-3d Ціна: %-8.2f Сума: %-9.2f Статус: %s",
            getId(), getName(), trip.getRoute(), seats, trip.getFinalPrice(), getTotalCost(), status.getTitle()
        );
    }
}
