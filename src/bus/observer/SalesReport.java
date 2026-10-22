package bus.observer;

import bus.model.Booking;
import bus.model.BookingStatus;

public class SalesReport implements BookingObserver {
    private int soldSeats;
    private int cancelledCount;
    private double revenue;

    @Override
    public void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus) {
        if (newStatus == BookingStatus.PAID) {
            soldSeats += booking.getSeats();
            revenue += booking.getTotalCost();
        } else if (newStatus == BookingStatus.CANCELLED) {
            cancelledCount++;
            if (oldStatus == BookingStatus.PAID) {
                soldSeats -= booking.getSeats();
                revenue -= booking.getTotalCost();
            }
        }
    }

    public void printReport() {
        System.out.println("----- Звіт продажів -----");
        System.out.println("Продано місць: " + soldSeats);
        System.out.println("Скасовано бронювань: " + cancelledCount);
        System.out.printf("Виручка: %.2f грн%n", revenue);
    }
}
