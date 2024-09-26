package us.smartmc.gamescore.util;

import com.google.gson.Gson;
import org.bukkit.Location;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.*;

public class CuboidSerializer {

    public static void serialize(Cuboid cuboid, String filePath) {
        CuboidWrapper blockState = new CuboidWrapper(cuboid);
        String json = new Gson().toJson(blockState);
        System.out.println("SERIALIZE BLOCKSTATE " + json);
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(blockState);
            System.out.println("Cuboid guardado en " + filePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CuboidWrapper deserializeAndPaste(String filePath, Location locationToPaste) {
        CuboidWrapper wrapper = deserialize(filePath);
        wrapper.pasteAtLocation(locationToPaste);
        return wrapper;
    }

    public static CuboidWrapper deserialize(String filePath) {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            CuboidWrapper wrapper = (CuboidWrapper) in.readObject();
            System.out.println("Wrapper restaurado desde " + filePath);
            return wrapper;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
