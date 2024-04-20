package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class HologramArmorStand {

    @Getter
    private final ArmorStand stand;
    private final String unformattedLine;

    protected HologramArmorStand(Location location, String line) {
        this.stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.unformattedLine = line;
        stand.setCustomName(line);
        stand.setCustomNameVisible(false);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setSmall(true);
    }



}
