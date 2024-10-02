package us.smartmc.test.instance;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.serialization.BlockStateWrapper;

import java.util.Map;


public class RegionPacketSender {

    public static void sendPasteChanges(Player player, Location location, CuboidPaster paster) {
        Map<Location, BlockStateWrapper> blocks = paster.getRelativeLocationsWithMaterials(location);
        final int batchSize = 1000;
        final int totalBlocks = blocks.size();

        // Crear un nuevo BukkitRunnable para enviar bloques
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                // Si hemos enviado todos los bloques, cancelar la tarea
                if (index >= totalBlocks) {
                    this.cancel();
                    return;
                }

                // Enviar bloques en lotes
                for (int i = 0; i < batchSize && index < totalBlocks; i++, index++) {
                    Map.Entry<Location, BlockStateWrapper> entry = (Map.Entry<Location, BlockStateWrapper>) blocks.entrySet().toArray()[index];
                    Location loc = entry.getKey();
                    BlockStateWrapper block = entry.getValue();
                    Material material = Material.getMaterial(block.getType());
                    byte materialData = block.getTypeData();
                    player.sendBlockChange(loc, material, materialData);
                }
            }
        }.runTaskTimer(GamesCoreAPI.getApi().getPlugin(), 0, 1);
    }

}
