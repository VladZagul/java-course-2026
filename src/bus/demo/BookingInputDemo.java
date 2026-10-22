package bus.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

import bus.exception.DomainException;
import bus.exception.InvalidPriceException;
import bus.exception.InvalidQuantityException;
import bus.model.Booking;
import bus.model.IntercityTrip;
import bus.model.Trip;

public class BookingInputDemo {

    public static Booking[] run(Scanner scanner) {
        int n = 0;
        boolean validCount = false;
        while (!validCount) {
            System.out.print("Скільки бронювань введемо? ");
            try {
                n = scanner.nextInt();
                scanner.nextLine();
                if (n > 0) {
                    validCount = true;
                } else {
                    System.out.println("Кількість бронювань має бути більшою за нуль.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести ціле число. Спробуйте ще раз.");
                scanner.nextLine();
            }
        }

        Booking[] bookings = new Booking[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Бронювання #" + (i + 1) + ":");

            System.out.print("  Ім'я пасажира: ");
            String passengerName = scanner.nextLine();

            System.out.print("  Маршрут: ");
            String route = scanner.nextLine();

            int seats = 0;
            double pricePerSeat = 0;
            try {
                System.out.print("  Кількість місць: ");
                seats = scanner.nextInt();

                System.out.print("  Ціна місця: ");
                pricePerSeat = scanner.nextDouble();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Помилка: кількість місць і ціна мають бути числами. Це бронювання пропущено.");
                scanner.nextLine();
                i--;
                continue;
            }

            try {
                bookings[i] = createBookingWithLogging(i + 1, passengerName, route, seats, pricePerSeat);
            } catch (InvalidQuantityException e) {
                System.out.println("Некоректна кількість місць (" + e.getInvalidValue() + "): " + e.getMessage());
                i--;
            } catch (InvalidPriceException e) {
                System.out.println("Некоректна ціна (" + e.getInvalidValue() + "): " + e.getMessage());
                i--;
            } catch (DomainException e) {
                System.out.println("Доменна помилка: " + e.getMessage());
                i--;
            }
        }

        System.out.println();
        System.out.println("===== Усі бронювання =====");
        for (Booking booking : bookings) {
            System.out.println(booking);
        }

        System.out.print("\nНа скільки пасажирів розділити загальну суму (для середнього)? ");
        try {
            int divisor = scanner.nextInt();
            scanner.nextLine();
            int totalCostInt = 0;
            for (Booking booking : bookings) {
                totalCostInt += (int) booking.getTotalCost();
            }
            int average = totalCostInt / divisor;
            System.out.println("Середня сума (ціла): " + average);
        } catch (ArithmeticException e) {
            System.out.println("Помилка: ділення на нуль неможливе. " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Помилка: потрібно було ввести ціле число.");
            scanner.nextLine();
        }

        System.out.print("\nВведіть індекс бронювання для перегляду: ");
        try {
            int index = scanner.nextInt();
            scanner.nextLine();
            Booking selected = bookings[index];
            System.out.println("Обране бронювання: " + selected);
        } catch (InputMismatchException e) {
            System.out.println("Помилка: індекс має бути цілим числом.");
            scanner.nextLine();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Помилка: такого індексу немає в масиві (допустимо 0.." + (bookings.length - 1) + ").");
        } catch (Exception e) {
            System.out.println("Непередбачена помилка: " + e.getMessage());
        }

        System.out.println("\n--- Демонстрація ієрархії власних винятків ---");
        DomainException[] demoExceptions = {
            new InvalidPriceException("демо: від'ємна ціна", -5.0),
            new InvalidQuantityException("демо: нуль місць", 0)
        };
        for (DomainException demo : demoExceptions) {
            try {
                throw demo;
            } catch (DomainException e) {
                System.out.println("Перехоплено як DomainException -> насправді: "
                        + e.getClass().getSimpleName() + " | " + e.getMessage());
            }
        }

        return bookings;
    }

    private static Booking createBookingWithLogging(long id, String passengerName, String route, int seats,
            double pricePerSeat) throws DomainException {
        try {
            LocalDateTime departure = LocalDate.now().plusDays(1).atTime(8, 0);
            Trip trip = new IntercityTrip(id, route, departure, pricePerSeat);
            return new Booking(id, passengerName, trip, seats);
        } catch (InvalidPriceException | InvalidQuantityException e) {
            System.out.println("[LOG] Під час створення бронювання сталася помилка: " + e.getMessage());
            throw e;
        }
    }
}
