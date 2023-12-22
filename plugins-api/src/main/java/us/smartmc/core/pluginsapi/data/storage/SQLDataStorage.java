
package us.smartmc.core.pluginsapi.data.storage;

import us.smartmc.core.pluginsapi.connection.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDataStorage implements ISQLDataStorage {

    private static final Connection connection = MySQLConnection.getConnection();

    private final String tableName;

    public SQLDataStorage(String table, String field, String... fields) {
        this.tableName = table;
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table);
        builder.append(" (").append(field).append(",");
        for (String f : fields) {
            builder.append(f);
            builder.append(",");
        }
        // delete last char (,):
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        MySQLConnection.executeQuery(builder.toString());
    }

    @Override
    public void set(ISaveDataData data) {
        if (!(data instanceof SQLSaveDataData)) return;

        SQLSaveDataData sqlData = (SQLSaveDataData) data;
        try {
            sqlData.executeInto(tableName, connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object get(String field, Object value, String columnLabel) {
        try {
            String selectQuery = "SELECT * FROM " + tableName + " WHERE " + field + " = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setObject(1, value);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getObject(columnLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object get(String path) {
        return null;
    }

    @Override
    public void delete(String field, Object value) throws SQLException {
        try {
            String deleteQuery = "DELETE FROM " + tableName + " WHERE " + field + " = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setObject(1, value);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(String path) {
    }

    public String getTableName() {
        return tableName;
    }
}
