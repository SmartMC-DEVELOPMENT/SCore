package us.smartmc.game.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldUtils {

    public static void loadChunksBetweenLocations(Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }

        if (!pos1.getWorld().equals(pos2.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world");
        }

        World world = pos1.getWorld();
        int x1 = pos1.getBlockX() >> 4;
        int z1 = pos1.getBlockZ() >> 4;
        int x2 = pos2.getBlockX() >> 4;
        int z2 = pos2.getBlockZ() >> 4;

        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Chunk chunk = world.getChunkAt(x, z);
                if (!chunk.isLoaded()) {
                    world.loadChunk(x, z, true);
                }
            }
        }
    }

}
