
package us.smartmc.core.pluginsapi.data.storage;

import com.google.gson.internal.LinkedTreeMap;
import us.smartmc.core.pluginsapi.connection.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLSaveDataData implements ISaveDataData {

    private final LinkedTreeMap<String, Object> map;
    private final boolean replaceData;

    public SQLSaveDataData(HashMap<String, Object> map, boolean replaceData) {
        this.map = new LinkedTreeMap<>();
        this.map.putAll(map);
        this.replaceData = replaceData;
    }

    public boolean isValueAlreadyInserted(String table, String field, Object value) throws Exception {
        String statement = "SELECT COUNT(*) FROM " + table + " WHERE " + field + "=?";
        PreparedStatement countStatement = MySQLConnection.getConnection().prepareStatement(statement);
        countStatement.setObject(1, value);
        ResultSet resultSet = countStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    public void executeInto(String table, Connection connection) throws Exception {
        StringBuilder builder = new StringBuilder("INSERT INTO " + table + " " + keysArg() +
                " " +
                valuesArg());

        if (replaceData) {
            builder.append(" ON DUPLICATE KEY UPDATE ");
            for (String key : map.keySet()) {
                builder.append(key + " = VALUES(" + key + "), ");
            }
            // delete last 2 chars (, ):
            builder.delete(builder.length() - 2, builder.length());
        }

        String statement = builder.toString();
        PreparedStatement insertStatement = connection.prepareStatement(statement);
        List<Object> objects = new ArrayList<>(map.values());
        for (int index = 1; index <= objects.size(); index++) {
            insertStatement.setObject(index, objects.get(index - 1));
        }
        insertStatement.executeUpdate();
    }

    private String valuesArg() {
        StringBuilder builder = new StringBuilder("VALUES (");
        for (int i = 0; i < map.size(); i++) {
            builder.append("?, ");
        }
        // delete last 2 chars (, ):
        builder.delete(builder.length() - 2, builder.length());
        builder.append(")");
        return builder.toString();
    }

    private String keysArg() {
        StringBuilder builder = new StringBuilder("(");
        for (String key : map.keySet()) {
            builder.append(key + ", ");
        }
        // delete last 2 chars (, ):
        builder.delete(builder.length() - 2, builder.length());
        builder.append(")");
        return builder.toString();
    }

}
