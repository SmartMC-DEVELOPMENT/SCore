package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import us.smartmc.core.SmartCore;

@Getter
public class HologramArmorStand {

    private final ArmorStand stand;

    private final String unformattedLine;

    public HologramArmorStand(Location location, String line) {
        this.unformattedLine = line;
        stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setCustomName(line);
        stand.setCustomNameVisible(false);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setSmall(false);
    }
}
