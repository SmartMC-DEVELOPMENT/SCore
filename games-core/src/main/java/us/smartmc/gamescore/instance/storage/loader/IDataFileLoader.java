package us.smartmc.gamescore.instance.storage.loader;

import java.io.File;
import java.io.IOException;

public interface IDataFileLoader extends IDataLoader<File> {

    static void createFiles(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
