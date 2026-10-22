package bus.service.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import bus.db.DBConnection;
import bus.exception.DataAccessException;
import bus.model.Booking;

public class BookingPlacementService {
    private final JdbcBookingService bookingService;
    private final JdbcSeatService seatService;

    public BookingPlacementService(JdbcBookingService bookingService, JdbcSeatService seatService) {
        this.bookingService = bookingService;
        this.seatService = seatService;
    }

    public void placeBooking(Booking booking) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                bookingService.save(connection, booking);
                seatService.decrease(connection, booking.getTrip().getId(), booking.getSeats());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DataAccessException("Бронювання #" + booking.getId()
                        + " не оформлено, зміни скасовано (rollback): " + explain(e), e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Помилка з'єднання з БД: " + e.getMessage(), e);
        }
    }

    private String explain(SQLException e) {
        if ("23513".equals(e.getSQLState())) {
            return "недостатньо вільних місць у рейсі";
        }
        return e.getMessage();
    }
}
