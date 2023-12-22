package us.smartmc.serverhandler.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import us.smartmc.serverhandler.ConsoleColors;
import us.smartmc.serverhandler.OrchestratorMain;
import us.smartmc.serverhandler.instance.Configuration;
import us.smartmc.serverhandler.instance.ServerInfo;
import us.smartmc.serverhandler.util.FileUtil;
import us.smartmc.serverhandler.util.ServerUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerManager {

    private static final HashMap<String, ServerInfo> servers = new HashMap<>();

    public static void create(String name) {
        Configuration configuration = Configuration.get(name);
        if (configuration == null) {
            OrchestratorMain.log(ConsoleColors.RED + "Not config with name " + name + " found!");
            return;
        }

        int count = getCountByConfigName(name);
        int portToHost = configuration.getData().getMinPort() + count;

        // Incrementa el puerto hasta encontrar uno disponible
        while (ServerUtil.isPortInUse(portToHost)) {
            portToHost++;
        }

        // Comprueba si el puerto está dentro del rango
        if (portToHost > configuration.getData().getMaxPort()) {
            OrchestratorMain.log(ConsoleColors.RED + "Create of server " + name + " has been cancelled due to max port exceeded!");
            return;
        }

        ServerInfo serverInfo = new ServerInfo(configuration, ServerInfo.getNextServerName(configuration), "127.0.0.1", portToHost);
        // COPY STARTUP
        FileUtil.createStartup(configuration.getData().getStartupDirectory(), serverInfo.getDirectory(),
                portToHost, serverInfo.getName());

        // COPY TEMPLATES
        FileUtil.copyTemplates(serverInfo.getDirectory(), configuration.getData().getTemplateDirectories());

        // START SERVER SCRIPT (START.SH)
        ServerUtil.startServer(serverInfo.getDirectory());

        servers.put(serverInfo.getName(), serverInfo);

        OrchestratorMain.log("Created new server named " + serverInfo.getName() + "!");
    }

    public static void register(String name, String hostname, int port) {
        Configuration configuration = ConfigManager.getByServerName(name);
        ServerInfo serverInfo = new ServerInfo(configuration, name, hostname, port);
        servers.put(name, serverInfo);
        BackendProxyConnectionHandler
                .broadcast("registerServer " + name + " " + hostname + " " + port);
    }

    public static ServerInfo get(String name) {
        return servers.get(name);
    }

    public static void deleteIfNotTemporalAndUnregister(String name) {
        ServerInfo serverInfo = get(name);
        if (serverInfo.getConfig().getData().isTemporal()) {
            FileUtil.deleteDirectory(serverInfo.getDirectory());
            System.out.println("Server " + name + " has inactivated! Deleted all files!");
        }

        unregister(name);
        BackendProxyConnectionHandler
                .broadcast("unregisterServer " + name);
    }

    private static void unregister(String name) {
        servers.remove(name);
    }

    public static int getCountByConfigName(String configName) {
        int number = 1;
        String prefix = configName + ServerUtil.COUNT_SEPARATOR;
        Set<String> serverNames =
                servers.keySet().stream().filter(s -> s.startsWith(prefix)).collect(Collectors.toSet());
        String currentName = prefix + number;
        while (serverNames.contains(currentName)) {
            number++;
            currentName = prefix + number;
        }
        return number;
    }

    public static String getServersAsJsonText() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Set<LinkedTreeMap<String, Object>> set = new HashSet<>();
        for (ServerInfo serverInfo : servers.values()) {
            LinkedTreeMap<String, Object> info = new LinkedTreeMap<>();
            info.put("name", serverInfo.getName());
            info.put("host", serverInfo.getHostname());
            info.put("port", serverInfo.getPort());
            set.add(info);
        }
        return gson.toJson(set);
    }

}
