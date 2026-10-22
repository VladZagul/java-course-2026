package bus.demo;

import bus.model.Booking;
import bus.model.BookingStatus;
import bus.observer.BookingLogger;
import bus.observer.PassengerNotifier;
import bus.observer.SalesReport;

public class StatusDemo {

    public static void run(Booking[] bookings) {
        BookingLogger logger = new BookingLogger();
        PassengerNotifier notifier = new PassengerNotifier();
        SalesReport report = new SalesReport();

        for (Booking booking : bookings) {
            booking.addObserver(logger);
            booking.addObserver(notifier);
            booking.addObserver(report);
        }

        System.out.println("\n===== Зміна статусів (Observer) =====");
        for (int i = 0; i < bookings.length; i++) {
            Booking booking = bookings[i];
            System.out.println("\n" + booking);
            booking.setStatus(BookingStatus.PAID);
            if (i == 0) {
                System.out.println("--- Пасажир відмовився від SMS: відписуємо PassengerNotifier ---");
                booking.removeObserver(notifier);
            }
            if (i % 2 == 0) {
                booking.setStatus(BookingStatus.BOARDED);
                booking.setStatus(BookingStatus.COMPLETED);
            } else {
                booking.setStatus(BookingStatus.CANCELLED);
            }
        }

        System.out.println();
        report.printReport();

        for (Booking booking : bookings) {
            booking.removeObserver(logger);
            booking.removeObserver(notifier);
            booking.removeObserver(report);
        }
    }
}
