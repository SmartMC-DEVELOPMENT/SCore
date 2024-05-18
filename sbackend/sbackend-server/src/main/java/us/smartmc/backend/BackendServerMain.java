package us.smartmc.backend;

import lombok.Getter;
import us.smartmc.backend.command.SubChannelCommand;
import us.smartmc.backend.command.SubContextCommand;
import us.smartmc.backend.command.UnsubChannelCommand;
import us.smartmc.backend.command.UnsubContextCommand;
import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.handler.LoginAuthManager;
import us.smartmc.backend.handler.ModulesHandler;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.backend.instance.config.JsonConfig;
import us.smartmc.backend.listener.BroadcastCommandListener;
import us.smartmc.backend.listener.BroadcastListener;
import us.smartmc.backend.listener.SubscriptionsListeners;
import us.smartmc.backend.service.SocialServices;
import us.smartmc.backend.util.ConsoleUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class BackendServerMain {

    @Getter
    private static File parentDirectory;

    @Getter
    private static BackendServer backendServer;

    private static JsonConfig mainConfig;

    private static ServicesManager serviceManager;

    public static void main(String[] args) throws Exception {
        parentDirectory = getJarParentDirectory();

        setupConfiguration();
        LoginAuthManager.loadAuthentifications();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new ModulesHandler().loadModulesJars();
            }
        }, 250);
        registerCommands();

        startBackendServer();
        startReadingConsoleInput();
    }

    private static void registerCommands() {
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

    private static void setupConfiguration() {
        mainConfig = new JsonConfig(new File(parentDirectory, "config.json"));
        mainConfig.load();
        mainConfig.registerDefaultValue("keystorePass", "P4ssw0rdS3cvre2024YTSMARTMCÑ");
        mainConfig.registerDefaultValue("logins-directory", "/home/network/sbackend/logins");
        mainConfig.registerDefaultValue("port", 7723);
        mainConfig.save();
    }

    private static void startReadingConsoleInput() {
        try {
            String line;
            while ((line = ConsoleUtil.readLine()) != null) {
                if (line.equals("end") || line.equals("stop")) {
                    System.exit(0);
                }
                ConsoleUtil.print("Comando ingresado: " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startBackendServer() throws IOException {
        backendServer = new BackendServer(((Number) mainConfig.get("port")).intValue());
        backendServer.start();
    }

    private static void registerServices() {
        ServicesManager.registerServices(true, new SocialServices());
        ServicesManager.get(SocialServices.class).getMessagesService().unload();
    }

    public static File getLoginsDirectory() {
        return new File((String) mainConfig.get("logins-directory"));
    }

    private static File getJarParentDirectory() throws URISyntaxException {
        String jarPath = BackendServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File jarFile = new File(jarPath);
        return new File(jarFile.getParent());
    }

}
