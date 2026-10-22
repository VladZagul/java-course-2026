package bus.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bus.exception.DataAccessException;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void init() {
        String script = readScript();
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            for (String sql : script.split(";")) {
                if (sql.isBlank()) {
                    continue;
                }
                try (PreparedStatement statement = connection.prepareStatement(sql.trim())) {
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Не вдалося виконати init.sql: " + e.getMessage(), e);
        }
    }

    private static String readScript() {
        try (InputStream in = DatabaseInitializer.class.getResourceAsStream("/init.sql")) {
            if (in == null) {
                throw new IllegalStateException("Не знайдено init.sql у resources");
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Не вдалося прочитати init.sql", e);
        }
    }
}
