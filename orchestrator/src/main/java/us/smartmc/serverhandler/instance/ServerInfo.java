package us.smartmc.serverhandler.instance;

import lombok.Getter;
import us.smartmc.serverhandler.config.ServerConfiguration;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.util.ServerUtil;

import java.io.File;

@Getter
public class ServerInfo extends CommonServerInfo {

    private final ServerConfiguration<?> config;
    private final File directory;

    public ServerInfo(ServerConfiguration<?> config, String name, String hostname, int port) {
        super(name, hostname, port);
        this.config = config;
        this.directory = new File(config.getData().getStorageParent() + "/" + name);
    }

    public static String getNextServerName(ServerConfiguration<?> configuration) {
        String configName = configuration.getName();
        int count = ServerManager.getCountByConfigName(configName);
        return configName + ServerUtil.COUNT_SEPARATOR + count;
    }

}
