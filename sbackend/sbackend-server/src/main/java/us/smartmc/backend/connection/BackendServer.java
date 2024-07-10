package us.smartmc.backend.connection;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.backend.BackendServerMain;
import us.smartmc.backend.command.SubChannelCommand;
import us.smartmc.backend.command.SubContextCommand;
import us.smartmc.backend.command.UnsubChannelCommand;
import us.smartmc.backend.command.UnsubContextCommand;
import us.smartmc.backend.handler.ConfigManager;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.handler.LoginAuthManager;
import us.smartmc.backend.handler.ModulesHandler;
import us.smartmc.backend.instance.config.JsonConfig;
import us.smartmc.backend.listener.BroadcastCommandListener;
import us.smartmc.backend.listener.BroadcastListener;
import us.smartmc.backend.listener.SubscriptionsListeners;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

@Getter
public class BackendServer extends Thread {

    @Setter
    private boolean active = true;
    private final ServerSocket serverSocket;

    public BackendServer(int port) throws IOException {
        ConfigManager.setupConfigurations();
        registerInputs();
        loadModules();
        LoginAuthManager.loadAuthentifications();

        serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en el puerto " + port + "...");
    }

    private static void loadModules() {
        new Thread(() -> {
            new ModulesHandler().loadModulesJars();
        }).start();
    }

    private static void registerInputs() {
        ConnectionInputManager.registerCommands(
                new SubChannelCommand(),
                new UnsubChannelCommand(),
                new SubContextCommand(),
                new UnsubContextCommand());

        ConnectionInputManager.registerListeners(
                new SubscriptionsListeners(),
                new BroadcastListener(),
                new BroadcastCommandListener());
    }

    @Override
    public void run() {
        while (active) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            acceptConnection(socket);
        }
    }

    private void acceptConnection(Socket socket) {
        System.out.println("New ConnectionRequest -> " + socket.getInetAddress().getHostAddress());
        try {
            new Thread(new ClientConnectionHandler(socket)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getLoginsDirectory() {
        return new File((String) ConfigManager.getMainConfig().get("logins-directory"));
    }

}
