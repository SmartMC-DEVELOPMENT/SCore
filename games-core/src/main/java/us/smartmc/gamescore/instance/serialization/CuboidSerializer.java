package us.smartmc.gamescore.instance.serialization;

import org.bukkit.Location;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.io.*;

public class CuboidSerializer {

    public void serialize(Cuboid cuboid, String filePath) {
        CuboidWrapper blockState = new CuboidWrapper(cuboid);
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(blockState);
            System.out.println("Cuboid guardado en " + filePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CuboidWrapper deserializeAndPaste(String filePath, Location locationToPaste) {
        CuboidWrapper wrapper = deserialize(filePath);
        wrapper.pasteAtLocation(locationToPaste);
        return wrapper;
    }

    public CuboidWrapper deserialize(String filePath) {
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
