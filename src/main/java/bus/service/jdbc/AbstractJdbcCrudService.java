package bus.service.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import bus.db.DBConnection;
import bus.exception.DataAccessException;
import bus.exception.EntityNotFoundException;
import bus.model.BaseEntity;
import bus.service.CRUDService;

public abstract class AbstractJdbcCrudService<T extends BaseEntity> implements CRUDService<T> {
    private final String entityName;

    protected AbstractJdbcCrudService(String entityName) {
        this.entityName = entityName;
    }

    protected abstract String getTableName();

    protected abstract String getSelectSql();

    protected abstract String getInsertSql();

    protected abstract String getUpdateSql();

    protected abstract String getNameColumn();

    protected abstract T mapRow(ResultSet rs) throws SQLException;

    protected abstract void setParameters(PreparedStatement statement, T entity) throws SQLException;

    protected Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public T findById(long id) {
        List<T> found = query(getSelectSql() + " WHERE id = ?", id);
        if (found.isEmpty()) {
            throw new EntityNotFoundException(entityName, id);
        }
        return found.get(0);
    }

    @Override
    public void save(T entity) {
        try (Connection connection = getConnection()) {
            save(connection, entity);
        } catch (SQLException e) {
            throw new DataAccessException("Не вдалося зберегти: " + entity + ". " + e.getMessage(), e);
        }
    }

    public void save(Connection connection, T entity) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(getUpdateSql())) {
            setParameters(update, entity);
            if (update.executeUpdate() > 0) {
                return;
            }
        }
        try (PreparedStatement insert = connection.prepareStatement(getInsertSql())) {
            setParameters(insert, entity);
            insert.executeUpdate();
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                throw new DataAccessException(entityName + " з id = " + id
                        + " не можна видалити: на нього посилаються інші записи", e);
            }
            throw new DataAccessException("Не вдалося видалити " + entityName + " з id = " + id, e);
        }
    }

    @Override
    public List<T> findAllSorted(Comparator<? super T> comparator) {
        List<T> result = query(getSelectSql());
        result.sort(comparator);
        return result;
    }

    @Override
    public List<T> findAllSortedByName() {
        return query(getSelectSql() + " ORDER BY " + getNameColumn() + ", id");
    }

    public List<T> findPage(int page, int pageSize) {
        return query(getSelectSql() + " ORDER BY id LIMIT ? OFFSET ?", pageSize, page * pageSize);
    }

    protected List<T> query(String sql, Object... params) {
        List<T> result = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Помилка запиту до таблиці " + getTableName() + ": " + e.getMessage(), e);
        }
        return result;
    }
}
