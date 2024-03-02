package us.smartmc.event.eventscore.types;

import org.bukkit.Material;
import us.smartmc.event.eventscore.instance.ToggleableItemInfo;

public enum EventWhitelistType {

    @ToggleableItemInfo(name = "&aPúblico", material = Material.GRASS)
    PUBLIC,
    NAME_LIST,
    ACCESS_CODE,

    private final char color;
    private final String name;
    private final Material material;

    EventWhitelistType(char color, String name, Material material) {
        this.color = color;
        this.name = name;
        this.material = material;
        EventWhitelistType.
    }


    @Override
    public void set(EventWhitelistType value) {

    }

    @Override
    public EventWhitelistType get() {
        return this;
    }

    @Override
    public Material getMaterial() {
        return null;
    }
}
