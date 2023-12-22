package us.smartmc.core.pluginsapi.util;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class FileUtil {

    public static void setup(File file) {
        file.getParentFile().mkdirs();
        if (file.exists()) return;
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
