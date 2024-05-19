package us.smartmc.serverhandler;

import com.google.gson.internal.LinkedTreeMap;
import me.imsergioh.jbackend.BackendServer;
import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.jbackend.api.manager.BackendActionManager;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.builder.DiscordLogEmbedBuilder;
import us.smartmc.serverhandler.config.DataConfiguration;
import us.smartmc.serverhandler.instance.FileConfigData;
import us.smartmc.serverhandler.manager.CacheCleanerManager;
import us.smartmc.serverhandler.manager.ConfigManager;
import us.smartmc.serverhandler.manager.ConsoleCommandManager;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.registration.CommandRegistration;
import us.smartmc.serverhandler.registration.CommonListenerRegistration;
import us.smartmc.serverhandler.registration.ConfigRegistration;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class OrchestratorMain {

    private static BackendServer backendServer;
    private static ConnectionHandler handler;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Registrations.register(
                CommandRegistration.class,
                CommonListenerRegistration.class,
                ConfigRegistration.class);
        BackendActionManager.registerConnectAction(h -> handler = h);
        DataConfiguration<FileConfigData> backendConnections = (DataConfiguration<FileConfigData>) ConfigManager.get("backend_connections");
        LinkedTreeMap<String, Object> redisDoc = (LinkedTreeMap<String, Object>) backendConnections.getData().getData().get("redis");
        RedisConnection.mainConnection = new RedisConnection((String) redisDoc.get("host"), ((Number) redisDoc.get("port")).intValue());

        // CREATE & START BACKEND SERVER
        backendServer = new BackendServer(55777);
        backendServer.start();

        new DiscordLogEmbedBuilder()
                .title("New Orchestrator connected!").description("Se ha abierto un \"orquestador\" para los servidores.")
                .addField("DIRECCIÓN IP", backendServer.getServerSocket().getLocalSocketAddress().toString())
                .addField("PUERTO", String.valueOf(backendServer.getServerSocket().getLocalPort()))
                .color("GREEN").send(RedisConnection.mainConnection.getResource());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CacheCleanerManager.checkServerCache();
            }
        }, 5000);

        ServerManager.startKeepAliveRemoveTask();


        // ESTA LÍNEA SIEMPRE AL FINAL!!!!
        startReadingConsoleInput();
    }

    public static File getParentFolder() {
        return new File(OrchestratorMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    }

    private static void startReadingConsoleInput() {
        while (true) {
            String input = readConsoleInput();
            ConsoleCommandManager.perform(input);
        }
    }

    public static String readConsoleInput() {
        System.out.print(">> ");
        return scanner.nextLine();
    }

    public static void log(String message) {
        System.out.println(ConsoleColors.BLUE_BOLD + "[LOG]" +
                ConsoleColors.WHITE + " " + message + ConsoleColors.RESET);
    }

    public static BackendServer getBackendServer() {
        return OrchestratorMain.backendServer;
    }

    public static ConnectionHandler getHandler() {
        return OrchestratorMain.handler;
    }
}