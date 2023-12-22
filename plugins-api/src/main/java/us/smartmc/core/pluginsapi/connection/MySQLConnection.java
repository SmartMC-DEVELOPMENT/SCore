package us.smartmc.core.pluginsapi.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnection {

    private static Connection connection;

    public static void startConnection(String host, int port, String database, String username, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTableIfNotExist(String name, String args) {
        executeQuery("CREATE TABLE if not exists " + name + " (" + args + ")");
    }

    public static void executeQuery(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("QUERY=" + query);
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
