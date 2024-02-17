package us.smartmc.serverhandler.config;

import com.google.gson.reflect.TypeToken;
import us.smartmc.serverhandler.instance.Configuration;
import us.smartmc.serverhandler.instance.FileConfigData;
import us.smartmc.serverhandler.manager.ConfigManager;

import java.lang.reflect.Type;

public class DataConfiguration<T extends FileConfigData> extends Configuration<T> {

    public DataConfiguration(String parentDir, String name) {
        super(parentDir, name);
    }

    public static DataConfiguration<?> get(String name) {
        return (DataConfiguration<?>) ConfigManager.get(name);
    }

    @Override
    public Type getDefaultType() {
        return new TypeToken<FileConfigData>(){}.getType();
    }
}
