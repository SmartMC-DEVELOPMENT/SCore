package us.smartmc.serverhandler;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.builder.DiscordLogEmbedBuilder;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.serverhandler.command.ExecuteServerMessageCommand;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import us.smartmc.serverhandler.listener.ACommandManagerListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerHandlerMain extends JavaPlugin {

    @Getter
    private static ServerHandlerMain plugin;

    @Getter
    private static BackendClient client;

    @Getter
    private static String serverID;
    @Getter
    private static String serverName;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

        ConnectionInputManager.registerListeners(new ACommandManagerListener());
        ConnectionInputManager.registerCommands(new ExecuteServerMessageCommand());

        try {
            serverID = readBackendProperty("server-id");
            serverName = readBackendProperty("server-name");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            client = new BackendClient("localhost", 55777);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();
            client.sendCommand("registerServer " + serverName + " " + getServer().getIp() + " " + Bukkit.getPort());
            client.sendCommand("serverStatus active " + serverName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*SyncUtil.later(() -> {
            new DiscordLogEmbedBuilder()
                    .title("Nuevo servidor conectado!").description("Se ha conectado un nuevo servidor correctamente a ServerHandler")
                    .addField("Nombre", serverName)
                    .addField("IP", "||" + Bukkit.getServer().getIp() + "||", true)
                    .addField("Puerto", String.valueOf(Bukkit.getPort()), true)
                    .color("GREEN").send(RedisConnection.mainConnection.getResource());
            plugin.getHandler().setCache("maxSlots." + serverID, Bukkit.getServer().getMaxPlayers());
        }, 250);*/
    }

    @Override
    public void onDisable() {
        getHandler().sendObject((new BackendCommandExecuteRequest("serverStatus idle " + serverName)));
        /*new DiscordLogEmbedBuilder()
                .title("Servidor desconectado!").description("Se ha desconectado un nuevo servidor correctamente de ServerHandler")
                .addField("Nombre", serverName)
                .addField("IP", "||" + Bukkit.getIp() + "||", true)
                .addField("Puerto", String.valueOf(Bukkit.getPort()), true)
                .color("RED").send(RedisConnection.mainConnection.getResource());*/
    }

    private String readBackendProperty(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(getDataFolder() + "/../../backend.properties"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(path + "=")) {
                return line.replaceFirst(path + "=", "");
            }
        }
        return null;
    }

    public ConnectionHandler getHandler() {
        return client.getInputStream().getConnectionHandler();
    }

}
