package bus.observer;

import bus.model.Booking;
import bus.model.BookingStatus;

public interface BookingObserver {
    void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus);
}
