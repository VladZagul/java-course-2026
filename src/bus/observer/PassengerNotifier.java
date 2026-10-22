package bus.observer;

import bus.model.Booking;
import bus.model.BookingStatus;

public class PassengerNotifier implements BookingObserver {

    @Override
    public void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus) {
        System.out.println("[SMS] " + booking.getPassengerName() + ", ваше бронювання на рейс \""
                + booking.getRoute() + "\": " + newStatus.getTitle());
    }
}
