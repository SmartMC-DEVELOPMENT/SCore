package us.smartmc.game.luckytowers.instance.map;

import org.bukkit.Location;
import org.bukkit.World;

public class MapsGeneration {

    private Location initLocation;

    public MapsGeneration(World world) {
        initLocation = world.getSpawnLocation();
    }
}
