package us.smartmc.backend;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.backend.command.GetPlayerDataCmd;
import us.smartmc.backend.command.HelloWorldCmd;
import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.AuthHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.handler.PlayerContextsHandler;
import us.smartmc.backend.instance.PlayerCache;
import us.smartmc.backend.instance.PlayerContext;
import us.smartmc.backend.instance.config.JsonConfig;

import java.io.File;
import java.net.URISyntaxException;
import java.util.UUID;

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

        cacheSergioData();

        // Al final de main: Crear BackendServer (Se quedará en hili principal aceptando conexiones)
        backendServer = new BackendServer(((Number) mainConfig.get("port")).intValue());
    }

    private static void cacheSergioData() {
        UUID id = UUID.fromString("5f257be9-0c62-4b17-ab8a-4ad53f9acb44");
        Document document = MongoDBConnection.mainConnection.getDatabase("player_data")
                .getCollection("core_players").find(new org.bson.Document("_id", id.toString())).first();
        PlayerCache playerCache = PlayerContextsHandler.getOrCreate(id).getCache();
        if (document == null) {
            System.out.println("No se ha podido cachear el data de sergio");
            return;
        }
        for (String key : document.keySet()) {
            playerCache.set(key, document.get(key));
        }
        System.out.println("Se ha cacheado a sergio");
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
