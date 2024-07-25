package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class HologramArmorStand {

    @Getter
    private final ArmorStand stand;

    @Getter
    private final String unformattedLine;

    public HologramArmorStand(Location location, String line) {
        this.unformattedLine = line;
        this.stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setCustomName(line);
        stand.setCustomNameVisible(true);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setSmall(false);
    }
}
