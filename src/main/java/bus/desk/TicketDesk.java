package bus.desk;

import bus.fare.Fare;
import bus.model.Booking;

public class TicketDesk {
    private Fare fare;

    public TicketDesk(Fare fare) {
        this.fare = fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public Fare getFare() {
        return fare;
    }

    public double calculateTotal(Booking booking) {
        return fare.calculateCost(booking);
    }

    public void printTicket(Booking booking) {
        System.out.println("----- Квиток -----");
        System.out.println(booking);
        System.out.println("Тариф: " + fare.getName() + (fare.requiresDocument() ? " (пред'явіть документ)" : ""));
        if (booking.getTrip().requiresPassport()) {
            System.out.println("Для посадки потрібен закордонний паспорт");
        }
        System.out.printf("До сплати: %.2f грн%n", calculateTotal(booking));
    }
}
