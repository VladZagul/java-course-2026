package bus.service.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import bus.exception.DataAccessException;
import bus.model.IntercityTrip;
import bus.model.InternationalTrip;
import bus.model.SuburbanTrip;
import bus.model.Trip;
import bus.service.TripService;

public class JdbcTripService extends AbstractJdbcCrudService<Trip> implements TripService {
    private static final int BATCH_SIZE = 500;

    public JdbcTripService() {
        super("Рейс");
    }

    @Override
    protected String getTableName() {
        return "trips";
    }

    @Override
    protected String getSelectSql() {
        return "SELECT id, route, trip_type, departure_time, price, stops_count, wifi, country, border_fee FROM trips";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO trips (route, trip_type, departure_time, price, stops_count, wifi, country, border_fee, id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE trips SET route = ?, trip_type = ?, departure_time = ?, price = ?, stops_count = ?, "
                + "wifi = ?, country = ?, border_fee = ? WHERE id = ?";
    }

    @Override
    protected String getNameColumn() {
        return "route";
    }

    @Override
    protected Trip mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String route = rs.getString("route");
        LocalDateTime departure = rs.getObject("departure_time", LocalDateTime.class);
        double price = rs.getBigDecimal("price").doubleValue();
        String type = rs.getString("trip_type");
        switch (type) {
            case "SUBURBAN":
                return new SuburbanTrip(id, route, departure, price, rs.getInt("stops_count"));
            case "INTERCITY":
                return new IntercityTrip(id, route, departure, price, rs.getBoolean("wifi"));
            case "INTERNATIONAL":
                return new InternationalTrip(id, route, departure, price, rs.getBoolean("wifi"),
                        rs.getString("country"), rs.getBigDecimal("border_fee").doubleValue());
            default:
                throw new DataAccessException("Невідомий тип рейсу в БД: " + type);
        }
    }

    @Override
    protected void setParameters(PreparedStatement statement, Trip trip) throws SQLException {
        statement.setString(1, trip.getRoute());
        statement.setObject(3, trip.getDepartureTime());
        statement.setBigDecimal(4, BigDecimal.valueOf(trip.getPrice()));
        switch (trip) {
            case InternationalTrip international -> {
                statement.setString(2, "INTERNATIONAL");
                statement.setNull(5, Types.INTEGER);
                statement.setBoolean(6, international.hasWifi());
                statement.setString(7, international.getCountry());
                statement.setBigDecimal(8, BigDecimal.valueOf(international.getBorderFee()));
            }
            case IntercityTrip intercity -> {
                statement.setString(2, "INTERCITY");
                statement.setNull(5, Types.INTEGER);
                statement.setBoolean(6, intercity.hasWifi());
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.NUMERIC);
            }
            case SuburbanTrip suburban -> {
                statement.setString(2, "SUBURBAN");
                statement.setInt(5, suburban.getStopsCount());
                statement.setNull(6, Types.BOOLEAN);
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.NUMERIC);
            }
        }
        statement.setLong(9, trip.getId());
    }

    @Override
    public List<Trip> findAllSortedByNameAndByDepartureTime() {
        return query(getSelectSql() + " ORDER BY route, departure_time, id");
    }

    public List<Trip> findByRoute(String route) {
        return query(getSelectSql() + " WHERE route = ?", route);
    }

    public void insertOneByOne(List<Trip> trips) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(getInsertSql())) {
            for (Trip trip : trips) {
                setParameters(statement, trip);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Помилка вставки по одному запису: " + e.getMessage(), e);
        }
    }

    public void insertBatch(List<Trip> trips) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(getInsertSql())) {
                int count = 0;
                for (Trip trip : trips) {
                    setParameters(statement, trip);
                    statement.addBatch();
                    count++;
                    if (count % BATCH_SIZE == 0) {
                        statement.executeBatch();
                    }
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Помилка пакетної вставки: " + e.getMessage(), e);
        }
    }

    public int deleteWithIdFrom(long fromId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM trips WHERE id >= ?")) {
            statement.setLong(1, fromId);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Не вдалося видалити тестові рейси: " + e.getMessage(), e);
        }
    }
}
