package us.smartmc.event.eventscore.types;

import org.bukkit.Material;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.util.ConfigPaths;

public enum PlayerGameModeType {

    CREATIVE('a', "Creativo", Material.BEDROCK),
    SURVIVAL('c', "Survival", Material.GRASS),
    ADVENTURE('a', "Aventura", Material.DIAMOND_BLOCK);

    private final char color;
    private final String name;
    private final Material material;

    PlayerGameModeType(char color, String name, Material material) {
        this.color = color;
        this.name = name;
        this.material = material;
    }
}
