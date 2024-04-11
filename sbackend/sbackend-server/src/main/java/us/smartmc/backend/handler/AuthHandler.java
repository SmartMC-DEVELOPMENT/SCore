package us.smartmc.backend.handler;

import us.smartmc.backend.BackendServerMain;
import us.smartmc.backend.instance.config.FileConfig;
import us.smartmc.backend.instance.config.JsonConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthHandler {

    private static final Map<String, String> logins = new HashMap<>();

    public static void loadCache() {
        // TODO: Cargar usuarios
        for (File file : Objects.requireNonNull(BackendServerMain.getLoginsDirectory().listFiles())) {
            FileConfig config = null;
            String fileName = file.getName();
            if (fileName.endsWith(".json")) config = new JsonConfig(file);
            assert config != null;
            config.load();
            String name = file.getName().split("\\.")[0];
            logins.put(name, (String) config.get("password"));
            System.out.println("Loaded user " + name);
        }
    }

    public static boolean checkLogin(String username, String password) {
        if (username == null) username = "default";
        String cachePassword = logins.get(username);
        if (password == null) return false;
        return cachePassword.equals(password);
    }

}
