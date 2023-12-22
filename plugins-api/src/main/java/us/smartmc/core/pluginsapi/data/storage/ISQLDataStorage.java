package us.smartmc.core.pluginsapi.data.storage;

import java.sql.SQLException;

public interface ISQLDataStorage extends IDataStorage {

    Object get(String field, Object value, String columnLabel);

    void delete(String field, Object value) throws SQLException;
}
