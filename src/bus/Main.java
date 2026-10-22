package bus;

import java.time.LocalDateTime;
import java.util.Scanner;

import bus.demo.BookingInputDemo;
import bus.demo.CrudDemo;
import bus.demo.FareDemo;
import bus.demo.StatusDemo;
import bus.demo.TripDemo;
import bus.model.Booking;
import bus.model.IntercityTrip;
import bus.model.InternationalTrip;
import bus.model.SuburbanTrip;
import bus.service.InMemoryCrudService;
import bus.service.InMemoryTripService;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Booking[] bookings = createSampleBookings();
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        bookings = BookingInputDemo.run(scanner);
                        break;
                    case "2":
                        printBookings(bookings);
                        break;
                    case "3":
                        FareDemo.run(scanner, bookings);
                        break;
                    case "4":
                        StatusDemo.run(bookings);
                        break;
                    case "5":
                        TripDemo.run();
                        break;
                    case "6":
                        CrudDemo.run(new InMemoryTripService(), new InMemoryCrudService<>("Бронювання"));
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Немає такого пункту меню.");
                }
            }
        } finally {
            System.out.println("\nРоботу програми завершено.");
            scanner.close();
        }
    }

    private static void printMenu() {
        System.out.println("\n========== МЕНЮ ==========");
        System.out.println("1 - Ввести бронювання (ЛР1-3)");
        System.out.println("2 - Показати бронювання");
        System.out.println("3 - Тарифи: Strategy, Factory, Adapter (ЛР4-5)");
        System.out.println("4 - Статуси бронювань: Observer (ЛР5)");
        System.out.println("5 - Ієрархія рейсів (ЛР6)");
        System.out.println("6 - CRUD-сервіс у пам'яті (ЛР7-9)");
        System.out.println("0 - Вихід");
        System.out.print("Ваш вибір: ");
    }

    private static void printBookings(Booking[] bookings) {
        System.out.println("\n===== Бронювання =====");
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    private static Booking[] createSampleBookings() {
        return new Booking[] {
            new Booking(1, "Олена Коваль",
                    new IntercityTrip(1, "Чернівці — Київ", LocalDateTime.of(2026, 11, 3, 7, 30), 780), 1),
            new Booking(2, "Андрій Бойко",
                    new IntercityTrip(2, "Чернівці — Львів", LocalDateTime.of(2026, 11, 3, 9, 15), 450, false), 6),
            new Booking(3, "Ірина Мельник",
                    new InternationalTrip(3, "Чернівці — Сучава", LocalDateTime.of(2026, 11, 4, 6, 0), 620, "Румунія"), 2),
            new Booking(4, "Петро Шевчук",
                    new SuburbanTrip(4, "Чернівці — Сторожинець", LocalDateTime.of(2026, 11, 3, 16, 40), 65, 12), 1)
        };
    }
}
