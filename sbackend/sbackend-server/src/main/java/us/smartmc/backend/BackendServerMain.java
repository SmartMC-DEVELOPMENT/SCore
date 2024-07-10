package us.smartmc.backend;

import lombok.Getter;
import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.ConfigManager;
import us.smartmc.backend.handler.ModulesHandler;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.backend.instance.config.JsonConfig;
import us.smartmc.backend.service.social.SocialServices;
import us.smartmc.backend.util.ConsoleUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class BackendServerMain {

    @Getter
    private static BackendServer backendServer;

    public static void main(String[] args) throws Exception {
        startBackendServer();
        startReadingConsoleInput();
    }

    private static void startReadingConsoleInput() {
        try {
            String line;
            while ((line = ConsoleUtil.readLine()) != null) {
                if (line.equalsIgnoreCase("end") || line.equalsIgnoreCase("stop")) {
                    System.exit(0);
                    return;
                }
                if (line.equalsIgnoreCase("reloadmodules")) {
                    new ModulesHandler().loadModulesJars();
                }
                ConsoleUtil.print("Comando ingresado: " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startBackendServer() throws IOException {
        ConfigManager.setupConfigurations();
        backendServer = new BackendServer(((Number) ConfigManager.getMainConfig().get("port")).intValue());
        backendServer.start();
    }
}

