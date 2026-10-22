package bus.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;

public final class DBConnection {
    private static DBConnection instance;

    private final JdbcConnectionPool dataSource;

    private DBConnection() {
        Properties properties = loadProperties();
        dataSource = JdbcConnectionPool.create(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password", ""));
        dataSource.setMaxConnections(Integer.parseInt(properties.getProperty("db.pool.max-connections", "10")));
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public static synchronized void shutdown() {
        if (instance != null) {
            instance.dataSource.dispose();
            instance = null;
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream in = DBConnection.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IllegalStateException("Не знайдено application.properties у resources");
            }
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Не вдалося прочитати application.properties", e);
        }
        return properties;
    }
}
