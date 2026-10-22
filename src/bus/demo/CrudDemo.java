package bus.demo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import bus.exception.EntityNotFoundException;
import bus.model.BaseEntity;
import bus.model.Booking;
import bus.model.IntercityTrip;
import bus.model.InternationalTrip;
import bus.model.SuburbanTrip;
import bus.model.Trip;
import bus.service.CRUDService;
import bus.service.EntityComparators;
import bus.service.TripService;
import bus.util.EntityPrinter;

public class CrudDemo {

    public static void run(TripService tripService, CRUDService<Booking> bookingService) {
        System.out.println("\n===== Додавання рейсів =====");
        Trip kyivEvening = new IntercityTrip(1, "Чернівці — Київ", LocalDateTime.of(2026, 11, 3, 21, 45), 820);
        Trip lviv = new IntercityTrip(2, "Чернівці — Львів", LocalDateTime.of(2026, 11, 3, 9, 15), 450, false);
        Trip kyivMorning = new IntercityTrip(3, "Чернівці — Київ", LocalDateTime.of(2026, 11, 3, 7, 30), 780);
        Trip suceava = new InternationalTrip(4, "Чернівці — Сучава", LocalDateTime.of(2026, 11, 4, 6, 0), 620, "Румунія");
        Trip khotyn = new SuburbanTrip(5, "Чернівці — Хотин", LocalDateTime.of(2026, 11, 3, 16, 40), 120, 8);

        tripService.save(kyivEvening);
        tripService.save(lviv);
        tripService.save(kyivMorning);
        tripService.save(suceava);
        tripService.save(khotyn);
        System.out.println("Записів у сховищі: " + tripService.findAllSortedByName().size());

        System.out.println("\nДо сортування (за id):");
        EntityPrinter.printAll(tripService.findAllSorted(EntityComparators.BY_ID));

        System.out.println("\nfindAllSortedByName():");
        EntityPrinter.printAll(tripService.findAllSortedByName());

        System.out.println("\nfindAllSortedByNameAndByDepartureTime():");
        EntityPrinter.printAll(tripService.findAllSortedByNameAndByDepartureTime());

        System.out.println("\nСховище після сортування не змінилося (за id):");
        EntityPrinter.printAll(tripService.findAllSorted(EntityComparators.BY_ID));

        System.out.println("\n===== Оновлення =====");
        tripService.save(new IntercityTrip(2, "Чернівці — Львів", LocalDateTime.of(2026, 11, 3, 10, 0), 480, true));
        System.out.println("Після save() з наявним id = 2: " + tripService.findById(2));
        System.out.println("Записів у сховищі: " + tripService.findAllSortedByName().size());

        System.out.println("\n===== Видалення =====");
        System.out.println("delete(5): " + tripService.delete(5));
        System.out.println("delete(5) ще раз: " + tripService.delete(5));

        System.out.println("\n===== Пошук неіснуючого id =====");
        try {
            tripService.findById(999);
        } catch (EntityNotFoundException e) {
            System.out.println("Помилка: " + e.getMessage() + " (id у винятку: " + e.getId() + ")");
        }

        System.out.println("\n===== Той самий сервіс для бронювань =====");
        bookingService.save(new Booking(1, "Петро Шевчук", tripService.findById(3), 1));
        bookingService.save(new Booking(2, "Андрій Бойко", tripService.findById(2), 3));
        bookingService.save(new Booking(3, "Олена Коваль", tripService.findById(4), 2));
        System.out.println("Бронювання за іменем пасажира:");
        EntityPrinter.printAll(bookingService.findAllSortedByName());
        System.out.println("Бронювання за сумою (Comparable<Booking>):");
        EntityPrinter.printAll(bookingService.findAllSorted(Comparator.naturalOrder()));
        System.out.println("findById(3): " + bookingService.findById(3));

        System.out.println("\n===== Comparator<? super T> =====");
        Comparator<BaseEntity> byIdDesc = EntityComparators.BY_ID.reversed();
        System.out.println("Один Comparator<BaseEntity> для рейсів:");
        EntityPrinter.printAll(tripService.findAllSorted(byIdDesc));
        System.out.println("...і для бронювань:");
        EntityPrinter.printAll(bookingService.findAllSorted(byIdDesc));

        System.out.println("\n===== printAll(Collection<? extends BaseEntity>) =====");
        List<Trip> trips = tripService.findAllSortedByName();
        List<Booking> bookings = bookingService.findAllSortedByName();
        System.out.println("List<Trip>:");
        EntityPrinter.printAll(trips);
        System.out.println("List<Booking>:");
        EntityPrinter.printAll(bookings);
    }
}
