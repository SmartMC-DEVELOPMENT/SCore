package us.smartmc.backend;

import lombok.Getter;
import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.*;
import us.smartmc.backend.instance.config.JsonConfig;
import us.smartmc.backend.listener.PlayerContextsListeners;
import us.smartmc.backend.listener.RequestPlayerCacheListener;
import us.smartmc.backend.service.TestPlayerService;
import us.smartmc.backend.util.ConsoleUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class BackendServerMain {

    @Getter
    private static File parentDirectory;

    @Getter
    private static BackendServer backendServer;

    private static JsonConfig mainConfig;

    public static void main(String[] args) throws Exception {
        parentDirectory = getJarParentDirectory();

        setupConfiguration();
        LoginAuthManager.loadAuthentifications();
        ModulesHandler.setup();
        ConnectionInputManager.registerListeners(new PlayerContextsListeners(), new RequestPlayerCacheListener());

        startBackendServer();
        startReadingConsoleInput();
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
    }

    private static void registerServices() {
        ServicesManager.registerServices(true, new TestPlayerService());
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
