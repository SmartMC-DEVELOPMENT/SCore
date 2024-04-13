package us.smartmc.snowgames.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldUtil {

    public static void unloadWorldByName(String name, boolean save) {
        World world = Bukkit.getWorld(name);
        if (world == null) return;
        Bukkit.unloadWorld(name, save);
    }

    public static World getWorldOrLoadByName(String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) return world;
        WorldCreator creator = new WorldCreator(name);
        return creator.createWorld();
    }

    public static void loadWorldByName(String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) return;
        WorldCreator creator = new WorldCreator(name);
        creator.createWorld();
    }

}
