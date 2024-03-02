package us.smartmc.event.eventscore.instance;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface IToggleableItem<T extends Enum<T>> extends IToggleableType<T> {

    Material getMaterial();

    char getColor();

    String getName();

    default ItemBuilder getItem() {
        ItemBuilder builder = ItemBuilder.of(getMaterial());
        String itemName = getColor() + getName();
        List<String> description = new ArrayList<>();
        try {
            T currentType = get();
            char color = getColor();
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

        return ItemBuilder.of(getMaterial())
                .name(itemName)
                .lore(description);
    }

    default ToggleableDescriptionType getDescriptionType() {
        return IToggleableType.getEnums(getEnumClass()).length >= 2 ?
                ToggleableDescriptionType.TOGGLEABLE_LIST : ToggleableDescriptionType.BOOLEAN;
    }

}
