package us.smartmc.serverhandler;

import lombok.Getter;
import me.imsergioh.jbackend.BackendConnection;
import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.jbackend.api.manager.BackendActionManager;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.builder.DiscordLogEmbedBuilder;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import us.smartmc.serverhandler.registration.CommandRegistration;
import us.smartmc.serverhandler.registration.CommonListenerRegistration;
import us.smartmc.serverhandler.util.ConnectionUtil;

import java.io.BufferedReader;
import java.io.FileReader;

public class ServerHandlerMain extends JavaPlugin {

    @Getter
    private static ServerHandlerMain plugin;

    @Getter
    private static BackendConnection connection;
    private static ConnectionHandler handler;
    @Getter
    private static String serverID;
    @Getter
    private static String serverName;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

        Registrations.register(
                CommonListenerRegistration.class,
                CommandRegistration.class
        );

        connection = new BackendConnection("localhost", 55777);
        connection.start();
        BackendActionManager.registerConnectAction(h -> {
            handler = h;
        });

        try {
            serverID = readServerProperty("server-id");
            serverName = readServerProperty("server-name");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        BackendActionManager.registerConnectAction(handler -> {
            ConnectionUtil.sendCommand(handler,
                    "registerServer " + serverName + " " + getServer().getIp() + " " + Bukkit.getPort());

            ConnectionUtil.sendCommand(handler,
                    "serverStatus active " + serverName);
        });

        SyncUtil.later(() -> {
            new DiscordLogEmbedBuilder()
                    .title("Nuevo servidor conectado!").description("Se ha conectado un nuevo servidor correctamente a ServerHandler")
                    .addField("Nombre", serverName)
                    .addField("IP", "||" + Bukkit.getServer().getIp() + "||", true)
                    .addField("Puerto", String.valueOf(Bukkit.getPort()), true)
                    .color("GREEN").send(RedisConnection.mainConnection.getResource());
            RedisConnection.mainConnection.getResource().set("maxSlots." + serverID, String.valueOf(Bukkit.getServer().getMaxPlayers()));
        }, 250);
    }

    @Override
    public void onDisable() {
        handler.send(new BackendCommandExecuteRequest("serverStatus idle " + serverName));
        new DiscordLogEmbedBuilder()
                .title("Servidor desconectado!").description("Se ha desconectado un nuevo servidor correctamente de ServerHandler")
                .addField("Nombre", serverName)
                .addField("IP", "||" + Bukkit.getIp() + "||", true)
                .addField("Puerto", String.valueOf(Bukkit.getPort()), true)
                .color("RED").send(RedisConnection.mainConnection.getResource());
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
}
