package us.smartmc.serverhandler.config;

import com.google.gson.reflect.TypeToken;
import us.smartmc.serverhandler.instance.Configuration;
import us.smartmc.serverhandler.instance.ServerConfigData;
import us.smartmc.serverhandler.manager.ConfigManager;

import java.lang.reflect.Type;

public class ServerConfiguration<T extends ServerConfigData> extends Configuration<T> {

    public ServerConfiguration(String parentDir, String name) {
        super(parentDir, name);
    }

    public static ServerConfiguration<ServerConfigData> get(String name) {
        return (ServerConfiguration<ServerConfigData>) ConfigManager.getByPrefixName(name);
    }

    @Override
    public Type getDefaultType() {
        return new TypeToken<ServerConfigData>(){}.getType();
    }
}
