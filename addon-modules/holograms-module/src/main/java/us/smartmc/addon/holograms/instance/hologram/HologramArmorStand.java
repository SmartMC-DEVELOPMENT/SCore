package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

@Getter
public class HologramArmorStand {

    private final ArmorStand stand;

    @Setter
    private String unformattedLine;

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
