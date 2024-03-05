package us.smartmc.event.eventscore.instance;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;

import java.util.List;

public class ToggleableItemInfo<T extends Enum<T>> {

    private final Material material;
    private final String displayName;

    private List<String> lore;

    public ToggleableItemInfo(Material material, String name) {
        this.material = material;
        this.displayName = name;
    }

    public ToggleableItemInfo<T> lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder getItemBuilder() {
        ItemBuilder builder = ItemBuilder.of(material).name(displayName);
        if (lore != null) builder.lore(lore);
        return builder;
    }

}
