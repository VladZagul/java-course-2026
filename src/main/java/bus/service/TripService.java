package bus.service;

import java.util.List;

import bus.model.Trip;

public interface TripService extends CRUDService<Trip> {
    List<Trip> findAllSortedByNameAndByDepartureTime();
}
