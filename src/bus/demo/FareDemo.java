package bus.demo;

import java.util.Arrays;
import java.util.Scanner;

import bus.desk.TicketDesk;
import bus.fare.Fare;
import bus.fare.FareFactory;
import bus.model.Booking;
import bus.model.Payable;

public class FareDemo {

    public static void run(Scanner scanner, Booking[] bookings) {
        Booking booking = chooseBooking(scanner, bookings);
        System.out.println("\nБронювання: " + booking);

        System.out.println("\n===== Тарифи =====");
        for (Fare fare : FareFactory.createAll()) {
            System.out.println(fare.describe(booking));
        }

        System.out.println("\n===== Бронювання, відсортовані за сумою (Comparable) =====");
        Booking[] sorted = Arrays.copyOf(bookings, bookings.length);
        Arrays.sort(sorted);
        for (Booking b : sorted) {
            System.out.println(b);
        }

        Payable[] payables = bookings;
        double total = 0;
        for (Payable payable : payables) {
            total += payable.getTotalCost();
        }
        System.out.printf("Загальна сума всіх бронювань: %.2f грн%n", total);

        System.out.println("\n===== Каса (Strategy) =====");
        TicketDesk desk = new TicketDesk(FareFactory.create("full"));
        desk.printTicket(booking);

        String codes = String.join(", ", FareFactory.getCodes());
        while (true) {
            System.out.print("\nКод тарифу (" + codes + ") або 0 - завершити: ");
            String code = scanner.nextLine();
            if (code.trim().equals("0")) {
                break;
            }
            try {
                desk.setFare(FareFactory.create(code));
                desk.printTicket(booking);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Booking chooseBooking(Scanner scanner, Booking[] bookings) {
        System.out.println("Бронювання:");
        for (int i = 0; i < bookings.length; i++) {
            System.out.println(i + " - " + bookings[i]);
        }
        while (true) {
            System.out.print("Оберіть номер бронювання: ");
            String line = scanner.nextLine().trim();
            try {
                return bookings[Integer.parseInt(line)];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Немає бронювання з номером " + line);
            }
        }
    }
}
