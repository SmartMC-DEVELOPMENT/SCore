package us.smartmc.game.luckytowers.instance.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.instance.game.GameTeam;

import java.util.*;

@Getter
public class PlayerBox {

    @Setter
    private Material material;

    private final Player player;

    public PlayerBox(Player player) {
        this.player = player;
    }

    public Collection<Location> complete(GameTeam team) {
        Location center = team.getSpawnAssigned();
        List<Location> locations = new ArrayList<>();

        // Definir las coordenadas de la caja
        int minX = center.getBlockX() - 1;
        int minY = center.getBlockY();
        int minZ = center.getBlockZ() - 1;
        int maxX = center.getBlockX() + 1;
        int maxY = center.getBlockY() + 2; // Sumar 2 para incluir el techo
        int maxZ = center.getBlockZ() + 1;

        // Crear las paredes laterales
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x == minX || x == maxX || y == minY || y == maxY) {
                    locations.add(new Location(center.getWorld(), x, y, minZ, center.getYaw(), center.getPitch()));
                    locations.add(new Location(center.getWorld(), x, y, maxZ, center.getYaw(), center.getPitch()));
                }
            }
        }

        // Crear el techo
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                locations.add(new Location(center.getWorld(), x, maxY, z, center.getYaw(), center.getPitch()));
            }
        }

        // Cambiar los materiales
        Material roofMaterial = material == Material.AIR ? Material.AIR : Material.BARRIER;
        locations.forEach(loc -> loc.getBlock().setType(loc.getBlockY() == maxY ? roofMaterial : material));
        return locations;
    }
}
