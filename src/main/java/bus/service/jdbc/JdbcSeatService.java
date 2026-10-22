package bus.service.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bus.db.DBConnection;
import bus.exception.DataAccessException;

public class JdbcSeatService {

    public void setFreeSeats(long tripId, int freeSeats) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            try (PreparedStatement update = connection.prepareStatement(
                    "UPDATE trip_seats SET free_seats = ? WHERE trip_id = ?")) {
                update.setInt(1, freeSeats);
                update.setLong(2, tripId);
                if (update.executeUpdate() > 0) {
                    return;
                }
            }
            try (PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO trip_seats (trip_id, free_seats) VALUES (?, ?)")) {
                insert.setLong(1, tripId);
                insert.setInt(2, freeSeats);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Не вдалося оновити вільні місця рейсу " + tripId + ": " + e.getMessage(), e);
        }
    }

    public int getFreeSeats(long tripId) {
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT free_seats FROM trip_seats WHERE trip_id = ?")) {
            statement.setLong(1, tripId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("free_seats") : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Не вдалося прочитати вільні місця рейсу " + tripId, e);
        }
    }

    public void decrease(Connection connection, long tripId, int seats) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE trip_seats SET free_seats = free_seats - ? WHERE trip_id = ?")) {
            statement.setInt(1, seats);
            statement.setLong(2, tripId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Для рейсу з id = " + tripId + " не задано кількість місць");
            }
        }
    }
}
