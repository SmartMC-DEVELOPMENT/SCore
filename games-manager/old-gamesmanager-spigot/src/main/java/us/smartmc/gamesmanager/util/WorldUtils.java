package us.smartmc.gamesmanager.util;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WorldUtils {

    private static final Set<World> tempWorlds = new HashSet<>();

    public static World clone(String original, String copyName) {
        World originalWorld = Bukkit.getWorld(original);

        if (originalWorld == null) {
            return null;
        }

        WorldCreator creator = new WorldCreator(copyName);
        World copyWorld = creator.createWorld();

        if (copyWorld != null) {
            copyWorld.getWorldFolder().mkdirs();
            originalWorld.save();
            originalWorld.getWorldFolder().renameTo(copyWorld.getWorldFolder());
            return copyWorld;
        }

        return null;
    }

    public static boolean copyWorldToDir(String worldName, String destinationFolderPath) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        File sourceFolder = world.getWorldFolder();
        File destinationFolder = new File(destinationFolderPath);

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        try {
            FileUtils.copyDirectory(sourceFolder, destinationFolder);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static World importWorldFromDir(String sourceFolderPath, String destinationName) {
        File sourceFolder = new File(sourceFolderPath);

        if (!sourceFolder.exists()) {
            return null; // La carpeta de origen no existe
        }

        WorldCreator creator = new WorldCreator(destinationName);
        World world = creator.createWorld();

        if (world != null) {
            File destinationFolder = world.getWorldFolder();
            if (destinationFolder.exists()) {
                Bukkit.unloadWorld(world, false);
                try {
                    FileUtils.copyDirectory(sourceFolder, destinationFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return Bukkit.getWorld(destinationName);
            } else {
                return null;
            }
        }
        return null;
    }

    public static void deleteTempWorld(World world) {
        File dir = world.getWorldFolder();
        Bukkit.unloadWorld(world.getName(), false);
        dir.delete();
    }

    public static void registerTempWorld(World world) {
        tempWorlds.add(world);
    }

    public static void deleteAllTempWorlds() {
        tempWorlds.forEach(WorldUtils::deleteTempWorld);
    }
}
