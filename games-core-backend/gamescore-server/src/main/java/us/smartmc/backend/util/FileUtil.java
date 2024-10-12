package us.smartmc.backend.util;

import java.io.File;

public class FileUtil {

    public static void createRequiredFilesFor(File file) {
        file.getParentFile().mkdirs();
    }

}
