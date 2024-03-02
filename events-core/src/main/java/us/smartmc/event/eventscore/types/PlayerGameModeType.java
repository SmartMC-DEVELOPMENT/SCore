package us.smartmc.event.eventscore.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.instance.IToggleableItem;
import us.smartmc.event.eventscore.instance.IToggleablePlayerItem;
import us.smartmc.event.eventscore.instance.IToggleablePlayerType;
import us.smartmc.event.eventscore.util.ConfigPaths;

public enum PlayerGameModeType implements IToggleableItem<PlayerGameModeType> {

    CREATIVE('a', "Creativo", Material.BEDROCK),
    SURVIVAL('c', "Survival", Material.GRASS),
    ADVENTURE('a', "Aventura", Material.DIAMOND_BLOCK);

    @Override
    public PlayerGameModeType get() {
        return EventsCore.getCore().getEventConfig().getEnumType(ConfigPaths.PLAYERS_GAMEMODE_KEY, PlayerGameModeType.class);
    }

    @Override
    public void set(PlayerGameModeType value) {
        EventsCore.getCore().getEventConfig().setEnumType(ConfigPaths.PLAYERS_GAMEMODE_KEY, value);
    }

    @Override
    public Class<PlayerGameModeType> getEnumClass() {
        return PlayerGameModeType.class;
    }

    private final char color;
    private final String name;
    private final Material material;

    PlayerGameModeType(char color, String name, Material material) {
        this.color = color;
        this.name = name;
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return get().material;
    }

    @Override
    public char getColor() {
        return get().color;
    }

    @Override
    public String getName() {
        return get().name;
    }
}
