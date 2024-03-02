package us.smartmc.event.eventscore.instance;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface IToggleablePlayerItem<T extends Enum<T>> extends IToggleablePlayerType<T> {

    Material getMaterial(Player player);

    char getColor(Player player);

    String getName(Player player);

    default ItemBuilder getItem(Player player) {
        ItemBuilder builder = ItemBuilder.of(getMaterial(player));
        String itemName = getColor(player) + getName(player);
        List<String> description = new ArrayList<>();
        try {
            T currentType = get(player);
            char color = getColor(player);
            for (T type : IToggleableType.getEnums(getEnumClass())) {
                Class<T> tClass = (Class<T>) type.getClass();
                try {
                    Field nameField = tClass.getDeclaredField("name");
                    String name = (String) nameField.get(type);
                    if (name == null) name = type.name();
                    if (currentType.equals(type)) {
                        description.add("&7 - " + color + name);
                    } else {
                        description.add("&7 - " + name);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            builder.lore(description);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ItemBuilder.of(getMaterial(player))
                .name(itemName)
                .lore(description);
    }

    default ToggleableDescriptionType getDescriptionType() {
        return IToggleableType.getEnums(getEnumClass()).length >= 2 ?
                ToggleableDescriptionType.TOGGLEABLE_LIST : ToggleableDescriptionType.BOOLEAN;
    }

}
