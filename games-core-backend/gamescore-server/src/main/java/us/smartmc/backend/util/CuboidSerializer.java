package us.smartmc.backend.util;

import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.manager.CacheCuboidManager;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.*;

public class CuboidSerializer {

    private static final File regionsDir;

    static {
        regionsDir = new File(BackendServer.getLoginsDirectory() + "/../regions");
        regionsDir.mkdirs();
    }

    public static void serialize(CuboidWrapper wrapper, File file) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(wrapper);
            CacheCuboidManager.saveCache(file.getName().split("\\.")[0], wrapper);
        }
    }

    public static CuboidWrapper deserialize(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            CuboidWrapper wrapper = (CuboidWrapper) in.readObject();
            CacheCuboidManager.saveCache(file.getName().split("\\.")[0], wrapper);
            return wrapper;
        }
    }

    public static File getRegionsDir() {
        return regionsDir;
    }
}
