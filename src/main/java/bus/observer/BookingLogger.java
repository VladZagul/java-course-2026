package bus.observer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import bus.model.Booking;
import bus.model.BookingStatus;

public class BookingLogger implements BookingObserver {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus) {
        System.out.println("[LOG " + LocalTime.now().format(TIME) + "] Бронювання пасажира "
                + booking.getPassengerName() + ": " + oldStatus.getTitle() + " -> " + newStatus.getTitle());
    }
}
