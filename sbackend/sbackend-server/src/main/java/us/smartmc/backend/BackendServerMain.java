package us.smartmc.backend;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import us.smartmc.backend.command.GetPlayerDataCmd;
import us.smartmc.backend.command.HelloWorldCmd;
import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.AuthHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.config.JsonConfig;
import us.smartmc.backend.util.ConsoleUtil;

import java.io.File;
import java.net.URISyntaxException;

public class BackendServerMain {

    @Getter
    private static File parentDirectory;

    @Getter
    private static BackendServer backendServer;

    private static JsonConfig mainConfig;

    public static void main(String[] args) throws Exception {
        parentDirectory = getJarParentDirectory();

        mainConfig = new JsonConfig(new File(parentDirectory, "config.json"));
        mainConfig.load();
        mainConfig.registerDefaultValue("keystorePass", "P4ssw0rdS3cvre2024YTSMARTMCÑ");
        mainConfig.registerDefaultValue("logins-directory", "/home/network/sbackend/logins");
        mainConfig.registerDefaultValue("port", 7723);
        mainConfig.save();

        System.out.println("BACKENDSERVERMAIN=" + mainConfig.get("logins-directory"));

        AuthHandler.loadCache();

        ConnectionInputManager.registerCommands(new HelloWorldCmd(), new GetPlayerDataCmd());

        MongoDBConnection.mainConnection = new MongoDBConnection("localhost", 27017);

        // Al final de main: Crear BackendServer (Se quedará en hili principal aceptando conexiones)
        backendServer = new BackendServer(((Number) mainConfig.get("port")).intValue());

        ConsoleUtil.print("&c¡Hola mundo pero en rojo! :D");

        try {
            String line;
            while ((line = ConsoleUtil.readLine()) != null) {
                ConsoleUtil.print("Comando ingresado: " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
