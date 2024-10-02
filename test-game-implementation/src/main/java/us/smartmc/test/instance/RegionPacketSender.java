package us.smartmc.test.instance;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.serialization.BlockStateWrapper;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;


public class RegionPacketSender {

    public static void sendPasteChanges(Player player, Location location, CuboidPaster paster) {

        for (Map.Entry<Location, BlockStateWrapper> entry : paster.getRelativeLocationsWithMaterials(location).entrySet()) {
            Location blockLoc = entry.getKey();
            BlockStateWrapper wrapper = entry.getValue();
            Material material = Material.getMaterial(wrapper.getType());
            byte materialData = wrapper.getTypeData();
        }

        .forEach((loc, wrapper) -> {

            player.sendBlockChange(loc, material, materialData);
        });
    }
}
