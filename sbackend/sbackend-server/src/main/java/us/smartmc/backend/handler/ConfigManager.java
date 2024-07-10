package us.smartmc.backend.handler;

import lombok.Getter;
import us.smartmc.backend.BackendServerMain;
import us.smartmc.backend.instance.config.JsonConfig;

import java.io.File;
import java.net.URISyntaxException;

public class ConfigManager {

    private static boolean alreadyLoaded;

    @Getter
    private static File parentDirectory;
    @Getter
    private static JsonConfig mainConfig;

    public static void setupConfigurations() {
        if (alreadyLoaded) return;
        alreadyLoaded = true;
        try {
            parentDirectory = getJarParentDirectory();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mainConfig = new JsonConfig(new File(parentDirectory, "config.json"));
        mainConfig.load();
        mainConfig.registerDefaultValue("logins-directory", "/home/network/sbackend/logins");
        mainConfig.registerDefaultValue("port", 7723);
        mainConfig.save();
    }

    private static File getJarParentDirectory() throws URISyntaxException {
        String jarPath = BackendServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File jarFile = new File(jarPath);
        return new File(jarFile.getParent());
    }

}

