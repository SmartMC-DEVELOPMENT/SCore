package us.smartmc.serverhandler.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import us.smartmc.serverhandler.ConsoleColors;
import us.smartmc.serverhandler.OrchestratorMain;
import us.smartmc.serverhandler.config.ServerConfiguration;
import us.smartmc.serverhandler.instance.ServerConfigData;
import us.smartmc.serverhandler.instance.ServerInfo;
import us.smartmc.serverhandler.util.FileUtil;
import us.smartmc.serverhandler.util.ServerUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ServerManager {

    private static final HashMap<String, ServerInfo> servers = new HashMap<>();

    public static boolean exists(String name) {
        return servers.containsKey(name);
    }

    public static void create(String templateName) {
        ServerConfiguration<ServerConfigData> configuration = ServerConfiguration.get(templateName);
        if (configuration == null) {
            OrchestratorMain.log(ConsoleColors.RED + "Not config with name " + templateName + " found!");
            return;
        }

        int count = getCountByConfigName(templateName);
        int portToHost = configuration.getData().getMinPort() + count;

        // Incrementa el puerto hasta encontrar uno disponible
        while (ServerUtil.isPortInUse(portToHost)) {
            portToHost++;
        }

        // Comprueba si el puerto está dentro del rango
        if (portToHost > configuration.getData().getMaxPort()) {
            OrchestratorMain.log(ConsoleColors.RED + "Create of server " + templateName + " has been cancelled due to max port exceeded!");
            return;
        }

        String serverName = ServerInfo.getNextServerName(configuration);

        ServerInfo serverInfo = new ServerInfo(configuration, serverName, "127.0.0.1", portToHost);
        String serverID = configuration.getData().getId_prefix() + serverName.replaceAll("[^0-9]", "");
        // COPY STARTUP
        FileUtil.createStartup(configuration.getData().getStartupDirectory(), serverInfo.getDirectory(),
                portToHost, serverInfo.getName(), serverID);

        // COPY TEMPLATES (INCLUDING SERVER.PROPERTIES IF AVAILABLE)

        FileUtil.copyTemplates(serverInfo.getDirectory(), configuration.getData().getTemplateDirectories(), portToHost, serverInfo.getName(), serverID);

        // START SERVER SCRIPT (START.SH)
        ServerUtil.startServer(serverInfo.getDirectory());

        servers.put(serverInfo.getName(), serverInfo);

        OrchestratorMain.log("Created new server named " + serverInfo.getName() + "!");
    }

    public static void register(String name, String hostname, int port) {
        ServerConfiguration<?> configuration = (ServerConfiguration<?>) ConfigManager.getByPrefixName(name);
        if (configuration == null) {
            return;
        }

        ServerInfo serverInfo = new ServerInfo(configuration, name, hostname, port);
        servers.put(name, serverInfo);
        BackendProxyConnectionHandler.broadcast("registerServer " + name + " " + hostname + " " + port);
    }

    public static ServerInfo get(String name) {
        if (!servers.containsKey(name)) create(name);
        return servers.get(name);
    }

    public static boolean isConnected(String name) {
        return servers.containsKey(name);
    }

    public static void deleteIfNotTemporalAndUnregister(String name) {
        ServerInfo serverInfo = get(name);
        if (!serverInfo.getConfig().getData().isPermanent()) {
            new Thread(() -> {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                FileUtil.deleteDirectory(serverInfo.getDirectory());
                System.out.println("Server " + name + " has inactivated! Deleted all files!");
            }).start();
        }
        unregister(name);
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

    public static boolean sendConsole(String command) {
        try {
            final Process sentCommandProcess = Runtime.getRuntime().exec(command);
            sentCommandProcess.getOutputStream().close();
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    public static CompletableFuture<Boolean> sendConsoleMessage(String screenName, String command) {
        return CompletableFuture.supplyAsync(() -> sendConsole("screen -S " + screenName + " -X stuff " + command + "^M"));
    }

    public static void stopServer(ServerInfo serverInfo) {
        final String screenName = serverInfo.getDirectory().getName();
        ServerManager.sendConsole("screen -XS " + screenName + " quit");
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
