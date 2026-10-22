package bus.service.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bus.model.Booking;
import bus.model.BookingStatus;
import bus.model.Trip;
import bus.service.TripService;

public class JdbcBookingService extends AbstractJdbcCrudService<Booking> {
    private final TripService tripService;

    public JdbcBookingService(TripService tripService) {
        super("Бронювання");
        this.tripService = tripService;
    }

    @Override
    protected String getTableName() {
        return "bookings";
    }

    @Override
    protected String getSelectSql() {
        return "SELECT id, passenger_name, trip_id, seats, status FROM bookings";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO bookings (passenger_name, trip_id, seats, status, id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE bookings SET passenger_name = ?, trip_id = ?, seats = ?, status = ? WHERE id = ?";
    }

    @Override
    protected String getNameColumn() {
        return "passenger_name";
    }

    @Override
    protected Booking mapRow(ResultSet rs) throws SQLException {
        Trip trip = tripService.findById(rs.getLong("trip_id"));
        return new Booking(
                rs.getLong("id"),
                rs.getString("passenger_name"),
                trip,
                rs.getInt("seats"),
                BookingStatus.valueOf(rs.getString("status")));
    }

    @Override
    protected void setParameters(PreparedStatement statement, Booking booking) throws SQLException {
        statement.setString(1, booking.getPassengerName());
        statement.setLong(2, booking.getTrip().getId());
        statement.setInt(3, booking.getSeats());
        statement.setString(4, booking.getStatus().name());
        statement.setLong(5, booking.getId());
    }
}
