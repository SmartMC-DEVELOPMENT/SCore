package us.smartmc.backend.util;

import us.smartmc.backend.connection.BackendServer;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.*;

public class CuboidSerializer {

    public static void serialize(CuboidWrapper wrapper, File file) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(wrapper);
            System.out.println("Cuboid guardado en " + file.getAbsolutePath());

        }
    }

    public static CuboidWrapper deserialize(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            CuboidWrapper wrapper = (CuboidWrapper) in.readObject();
            System.out.println("Wrapper restaurado desde " + file.getAbsolutePath());
            return wrapper;
        }
    }

    public static File getRegionsDir() {
        File appDir =  new File(BackendServer.getLoginsDirectory() + "/..");
        return new File(appDir + "/regions");
    }

}
