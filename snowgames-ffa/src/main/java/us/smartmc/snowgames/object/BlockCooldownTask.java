package us.smartmc.snowgames.object;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import us.smartmc.snowgames.FFAPlugin;

import java.util.TimerTask;

public class BlockCooldownTask extends TimerTask {

    @Getter
    private final Location location;
    private final Material recoverMaterial;

    public BlockCooldownTask(BlockState block) {
        this.location = block.getLocation();
        this.recoverMaterial = block.getType();
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTask(FFAPlugin.getFFAPlugin(), this::reset);
    }

    public void reset() {
        location.getWorld().getBlockAt(location).setType(recoverMaterial);
    }

}
