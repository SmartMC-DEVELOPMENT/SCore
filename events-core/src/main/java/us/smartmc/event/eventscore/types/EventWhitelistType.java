package us.smartmc.event.eventscore.types;

import org.bukkit.Material;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.instance.IToggleableItem;
import us.smartmc.event.eventscore.util.ConfigPaths;

public enum EventWhitelistType implements IToggleableItem<EventWhitelistType> {

    PUBLIC('a', "Público", Material.GREEN_DYE),
    NAME_LIST('e', "Lista blanca", Material.WHITE_DYE),
    ACCESS_CODE('c', "Código de acceso", Material.NAME_TAG);

    @Override
    public EventWhitelistType get() {
        return EventsCore.getCore().getEventConfig().getEnumType(ConfigPaths.WHITELISTMODE_KEY, EventWhitelistType.class);
    }

    @Override
    public void set(EventWhitelistType value) {
        EventsCore.getCore().getEventConfig().setEnumType(ConfigPaths.WHITELISTMODE_KEY, value);
    }

    @Override
    public Class<EventWhitelistType> getEnumClass() {
        return EventWhitelistType.class;
    }

    private final char color;
    private final String name;
    private final Material material;

    EventWhitelistType(char color, String name, Material material) {
        this.color = color;
        this.name = name;
        this.material = material;
    }

    @Override
    public String getName() {
        return get().name;
    }

    @Override
    public Material getMaterial() {
        return get().material;
    }

    @Override
    public char getColor() {
        return color;
    }
}
