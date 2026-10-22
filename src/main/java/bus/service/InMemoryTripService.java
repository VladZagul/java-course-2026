package bus.service;

import java.util.Comparator;
import java.util.List;

import bus.model.Trip;

public class InMemoryTripService extends InMemoryCrudService<Trip> implements TripService {

    public InMemoryTripService() {
        super("Рейс");
    }

    @Override
    public List<Trip> findAllSortedByNameAndByDepartureTime() {
        Comparator<Trip> comparator = EntityComparators.<Trip>byName()
                .thenComparing(Trip::getDepartureTime);
        return findAllSorted(comparator);
    }
}
