package us.smartmc.game.luckytowers.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BlockUtils {

    public static void sendSimpleEncapsulation(Player player, Material material) {
        for (Location location : getSimpleEncapsulationLocations(player.getLocation())) {
            player.sendBlockChange(location, material.createBlockData());
        }
    }

    private static Set<Location> getSimpleEncapsulationLocations(Location location) {
        Set<Location> list = new HashSet<>();
        list.add(location.clone().add(1, 0, 0));
        list.add(location.clone().add(-1, 0, 0));

        list.add(location.clone().add(0, 2, 0));

        list.add(location.clone().add(0, 0, 1));
        list.add(location.clone().add(0, 0, -1));
        return list;
    }

}
