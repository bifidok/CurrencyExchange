package com.example.currencyexchange.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnector {
    private static final Path PROPERTIES_PATH = Path.of("C:\\Users\\striz\\IdeaProjects\\CurrencyExchange" +
            "\\src\\main\\resources\\db.yml");
    private static String URL = "";
    private static String USER = "";
    private static String PASSWORD = "";
    private static Connection connection;

    static {
        try (InputStream stream = new FileInputStream(PROPERTIES_PATH.toFile())) {
            Class.forName("org.postgresql.Driver");
            Yaml yaml = new Yaml();
            Map<String, Object> obj = yaml.load(stream);
            URL = (String) obj.get("url");
            USER = (String) obj.get("user");
            PASSWORD = (String) obj.get("password");
            connection = createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DatabaseConnector() {

    }

    public static Connection getConnection() {
        return connection;
    }

    private static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }
}
