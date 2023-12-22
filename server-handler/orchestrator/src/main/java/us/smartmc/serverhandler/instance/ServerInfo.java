package us.smartmc.serverhandler.instance;

import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.util.ServerUtil;

import java.io.File;

public class ServerInfo extends CommonServerInfo {

    private final Configuration config;
    private final File directory;

    public ServerInfo(Configuration config, String name, String hostname, int port) {
        super(name, hostname, port);
        this.config = config;
        this.directory = new File(config.getData().getStorageParent() + "/" + name);
    }

    public File getDirectory() {
        return directory;
    }

    public Configuration getConfig() {
        return config;
    }

    public static String getNextServerName(Configuration configuration) {
        String configName = configuration.getName();
        int count = ServerManager.getCountByConfigName(configName);
        return configName + ServerUtil.COUNT_SEPARATOR + count;
    }

}
