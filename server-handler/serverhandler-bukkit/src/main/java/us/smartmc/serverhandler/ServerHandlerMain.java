package us.smartmc.serverhandler;

import me.imsergioh.jbackend.BackendConnection;
import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.jbackend.api.manager.BackendActionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import us.smartmc.serverhandler.registration.CommonListenerRegistration;
import us.smartmc.serverhandler.util.ConnectionUtil;

import java.io.BufferedReader;
import java.io.FileReader;

public class ServerHandlerMain extends JavaPlugin {

    private static ServerHandlerMain plugin;

    private static BackendConnection connection;
    private static ConnectionHandler handler;
    private static String serverID;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

        Registrations.register(CommonListenerRegistration.class);

        connection = new BackendConnection("localhost", 55777);
        connection.start();
        BackendActionManager.registerConnectAction(h -> {
            handler = h;
        });

        try {
            serverID = readServerProperty("server-id");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        BackendActionManager.registerConnectAction(handler -> {
            ConnectionUtil.sendCommand(handler,
                    "registerServer " + serverID + " " + getServer().getIp() + " " + Bukkit.getPort());

            ConnectionUtil.sendCommand(handler,
                    "serverStatus active " + serverID);
        });
    }

    @Override
    public void onDisable() {
        handler.send(new BackendCommandExecuteRequest("serverStatus idle " + serverID));
    }

    private String readServerProperty(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(getDataFolder() + "/../../server.properties"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(path + "=")) {
                return line.replaceFirst(path + "=", "");
            }
        }
        return null;
    }

    public static ConnectionHandler getHandler() {
        return handler;
    }

    public static BackendConnection getConnection() {
        return connection;
    }

    public static ServerHandlerMain getPlugin() {
        return plugin;
    }
}
