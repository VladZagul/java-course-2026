package bus.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import bus.exception.DataAccessException;
import bus.exception.EntityNotFoundException;
import bus.model.Booking;
import bus.model.IntercityTrip;
import bus.model.InternationalTrip;
import bus.model.Trip;
import bus.service.jdbc.BookingPlacementService;
import bus.service.jdbc.JdbcBookingService;
import bus.service.jdbc.JdbcSeatService;
import bus.service.jdbc.JdbcTripService;
import bus.util.EntityPrinter;

public class DbDemo {
    private static final long BENCHMARK_FIRST_ID = 100_000;

    public static void runTransaction() {
        JdbcTripService tripService = new JdbcTripService();
        JdbcBookingService bookingService = new JdbcBookingService(tripService);
        JdbcSeatService seatService = new JdbcSeatService();
        BookingPlacementService placementService = new BookingPlacementService(bookingService, seatService);

        Trip warsaw = new InternationalTrip(50, "Чернівці — Варшава", LocalDateTime.of(2026, 11, 5, 20, 0),
                1650, true, "Польща", 200);
        tripService.save(warsaw);
        bookingService.delete(501);
        bookingService.delete(502);
        seatService.setFreeSeats(warsaw.getId(), 5);

        System.out.println("\n===== Транзакція: бронювання місць =====");
        System.out.println("Рейс: " + warsaw);
        System.out.println("Вільних місць: " + seatService.getFreeSeats(warsaw.getId()));

        System.out.println("\nБронювання #501 на 2 місця");
        placementService.placeBooking(new Booking(501, "Марія Ткачук", warsaw, 2));
        System.out.println("Оформлено. Вільних місць: " + seatService.getFreeSeats(warsaw.getId()));
        System.out.println(bookingService.findById(501));

        System.out.println("\nБронювання #502 на 10 місць");
        try {
            placementService.placeBooking(new Booking(502, "Тарас Гнатюк", warsaw, 10));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Вільних місць після відкату: " + seatService.getFreeSeats(warsaw.getId()));
        try {
            bookingService.findById(502);
        } catch (EntityNotFoundException e) {
            System.out.println("У таблиці bookings рядка немає: " + e.getMessage());
        }

        System.out.println("\n===== ON DELETE RESTRICT =====");
        try {
            tripService.delete(warsaw.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void runSqlInjection() {
        JdbcTripService tripService = new JdbcTripService();
        tripService.save(new InternationalTrip(4, "Чернівці — Сучава", LocalDateTime.of(2026, 11, 4, 6, 0), 620, "Румунія"));

        System.out.println("\n===== PreparedStatement і SQL-ін'єкція =====");
        String normal = "Чернівці — Сучава";
        System.out.println("findByRoute(\"" + normal + "\"):");
        EntityPrinter.printAll(tripService.findByRoute(normal));

        String attack = "' OR '1'='1";
        System.out.println("\nfindByRoute(\"" + attack + "\"):");
        EntityPrinter.printAll(tripService.findByRoute(attack));
        System.out.println("Рядок передано як значення параметра, тому БД шукала рейс з таким маршрутом, а не виконала OR.");
    }

    public static void runBatchBenchmark() {
        JdbcTripService tripService = new JdbcTripService();
        int count = 5000;
        tripService.deleteWithIdFrom(BENCHMARK_FIRST_ID);

        System.out.println("\n===== Вставка " + count + " рейсів =====");
        List<Trip> first = createTestTrips(BENCHMARK_FIRST_ID, count);
        long start = System.nanoTime();
        tripService.insertOneByOne(first);
        long oneByOneMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("По одному запису: " + oneByOneMs + " мс");
        tripService.deleteWithIdFrom(BENCHMARK_FIRST_ID);

        List<Trip> second = createTestTrips(BENCHMARK_FIRST_ID, count);
        start = System.nanoTime();
        tripService.insertBatch(second);
        long batchMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Пакетом (addBatch/executeBatch): " + batchMs + " мс");

        if (batchMs > 0) {
            System.out.printf("Пакетна вставка швидша в %.1f раза%n", (double) oneByOneMs / batchMs);
        }
        int removed = tripService.deleteWithIdFrom(BENCHMARK_FIRST_ID);
        System.out.println("Тестові записи видалено: " + removed);
    }

    public static void runPagination() {
        JdbcTripService tripService = new JdbcTripService();
        int pageSize = 3;
        System.out.println("\n===== Посторінкове читання рейсів (по " + pageSize + ") =====");
        int page = 0;
        List<Trip> items = tripService.findPage(page, pageSize);
        if (items.isEmpty()) {
            System.out.println("Таблиця порожня. Спершу запустіть пункт 7.");
        }
        while (!items.isEmpty()) {
            System.out.println("Сторінка " + (page + 1) + ":");
            EntityPrinter.printAll(items);
            page++;
            items = tripService.findPage(page, pageSize);
        }
    }

    private static List<Trip> createTestTrips(long firstId, int count) {
        List<Trip> trips = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2027, 1, 1, 0, 0);
        for (int i = 0; i < count; i++) {
            trips.add(new IntercityTrip(firstId + i, "Тестовий рейс " + (firstId + i), start.plusMinutes(i),
                    300 + i % 50));
        }
        return trips;
    }
}
