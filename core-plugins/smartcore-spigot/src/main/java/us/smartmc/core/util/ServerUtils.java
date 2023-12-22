package us.smartmc.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ServerUtils {

    public static String readServerProperty(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(getServerRootFolder() + "/server.properties"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(path + "=")) {
                return line.replaceFirst(path + "=", "");
            }
        }
        return null;
    }

    public static File getServerRootFolder() {
        return new File(".");
    }

}
