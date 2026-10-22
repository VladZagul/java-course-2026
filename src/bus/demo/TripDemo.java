package bus.demo;

import java.time.LocalDateTime;

import bus.exception.DomainException;
import bus.model.Booking;
import bus.model.IntercityTrip;
import bus.model.InternationalTrip;
import bus.model.SuburbanTrip;
import bus.model.Trip;

public class TripDemo {
    private static final LocalDateTime MORNING = LocalDateTime.of(2026, 11, 3, 7, 30);
    private static final LocalDateTime EVENING = LocalDateTime.of(2026, 11, 3, 21, 45);

    public static void run() {
        try {
            showPolymorphism();
            showConstructorChain();
            showStaticAndDynamicType();
            showEquals();
            showComposition();
        } catch (DomainException e) {
            System.out.println("Помилка в тестових даних: " + e.getMessage());
        }
    }

    private static void showPolymorphism() throws DomainException {
        System.out.println("\n===== Поліморфізм: масив Trip[] =====");
        Trip[] trips = {
            new SuburbanTrip("Чернівці — Сторожинець", MORNING, 65, 12),
            new IntercityTrip("Чернівці — Київ", MORNING, 780),
            new InternationalTrip("Чернівці — Сучава", EVENING, 620, "Румунія"),
            new IntercityTrip("Чернівці — Львів", EVENING, 450, false)
        };
        for (Trip trip : trips) {
            System.out.println(trip);
            System.out.printf("   багаж: %d кг, ціна квитка зі зборами: %.2f грн, документи: %s%n",
                    trip.getBaggageAllowanceKg(), trip.getFinalPrice(), describeDocuments(trip));
        }
    }

    private static void showConstructorChain() throws DomainException {
        System.out.println("\n===== Ланцюжок конструкторів =====");
        Trip.setTraceConstructors(true);
        try {
            System.out.println("new InternationalTrip(\"Чернівці — Варшава\", ..., 1650, true, \"Польща\", 200):");
            new InternationalTrip("Чернівці — Варшава", EVENING, 1650, true, "Польща", 200);
            System.out.println("new InternationalTrip(\"Чернівці — Ясси\", ..., 900, \"Румунія\") -> this(...) зі збором за замовчуванням:");
            new InternationalTrip("Чернівці — Ясси", MORNING, 900, "Румунія");
            System.out.println("new IntercityTrip(\"Чернівці — Одеса\", ..., 1100) -> this(...) з Wi-Fi за замовчуванням:");
            new IntercityTrip("Чернівці — Одеса", EVENING, 1100);
        } finally {
            Trip.setTraceConstructors(false);
        }
    }

    private static void showStaticAndDynamicType() throws DomainException {
        System.out.println("\n===== Статичний і динамічний тип =====");
        Trip[] trips = {
            new InternationalTrip("Чернівці — Сучава", EVENING, 620, "Румунія"),
            new SuburbanTrip("Чернівці — Хотин", MORNING, 120, 8),
            new IntercityTrip("Чернівці — Київ", MORNING, 780)
        };
        for (Trip trip : trips) {
            System.out.println("Оголошений тип: Trip, фактичний: " + trip.getClass().getSimpleName());
            if (trip instanceof InternationalTrip international) {
                System.out.println("   перетин кордону, країна: " + international.getCountry());
            } else if (trip instanceof SuburbanTrip suburban) {
                System.out.println("   зупинок по дорозі: " + suburban.getStopsCount());
            } else {
                IntercityTrip intercity = (IntercityTrip) trip;
                System.out.println("   Wi-Fi в салоні: " + (intercity.hasWifi() ? "так" : "ні"));
            }
        }
    }

    private static void showEquals() throws DomainException {
        System.out.println("\n===== equals() і симетрія =====");
        IntercityTrip trip = new IntercityTrip("Чернівці — Сучава", EVENING, 620);
        IntercityTrip sameTrip = new IntercityTrip("Чернівці — Сучава", EVENING, 620);
        InternationalTrip internationalTrip = new InternationalTrip("Чернівці — Сучава", EVENING, 620, true, "Румунія", 0);

        System.out.println("trip.equals(sameTrip): " + trip.equals(sameTrip));
        System.out.println("hashCode однакові: " + (trip.hashCode() == sameTrip.hashCode()));
        System.out.println("trip.equals(internationalTrip): " + trip.equals(internationalTrip));
        System.out.println("internationalTrip.equals(trip): " + internationalTrip.equals(trip));
    }

    private static void showComposition() throws DomainException {
        System.out.println("\n===== Композиція: бронювання містить рейс =====");
        Trip trip = new InternationalTrip("Чернівці — Сучава", EVENING, 620, "Румунія");
        Booking booking = new Booking("Ірина Мельник", trip, 2);
        System.out.println(booking);
        System.out.println("Рейс у бронюванні: " + booking.getTrip());
        System.out.printf("Багаж на всіх: %d кг%n", booking.getSeats() * trip.getBaggageAllowanceKg());
    }

    private static String describeDocuments(Trip trip) {
        return switch (trip) {
            case InternationalTrip international -> "закордонний паспорт (" + international.getCountry() + ")";
            case IntercityTrip intercity -> "квиток і посвідчення особи";
            case SuburbanTrip suburban -> "лише квиток, можна купити у водія";
        };
    }
}
