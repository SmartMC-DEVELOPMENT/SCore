package us.smartmc.event.eventscore.instance;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.smartmc.event.eventscore.handler.MainEventHandler;
import us.smartmc.event.eventscore.util.EnumUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalToggleableItem<T extends Enum<T>> implements IToggleableType<T> {

    private final String path;
    private final Class<T> classType;

    private final Map<T, ToggleableItemInfo<T>> items = new HashMap<>();

    public GlobalToggleableItem(String path, Class<T> classType) {
        this.path = path;
        this.classType = classType;
    }

    public GlobalToggleableItem<T> setAdditionalAction(Consumer<T> consumer) {
        consumer.accept(getCurrentType());
        return this;
    }

    public ItemStack getItem() {
        return getItemBuilder().get();
    }

    public ItemBuilder getItemBuilder() {
        return get(getCurrentType()).getItemBuilder();
    }

    public ToggleableItemInfo<T> get(T enumValue) {
        return items.get(enumValue);
    }

    public GlobalToggleableItem<T> register(T enumValue, Material material, String displayName, String... descriptionLines) {
        ToggleableItemInfo<T> itemInfo = new ToggleableItemInfo<T>(material, displayName).lore(Arrays.asList(descriptionLines));
        items.put(enumValue, itemInfo);
        return this;
    }

    @Override
    public T toggleType() {
        T nextValue = EnumUtils.getNextValue(classType, getCurrentType());
        MainEventHandler.setEnumType(path, nextValue);
        return nextValue;
    }

    @Override
    public T getCurrentType() {
        return MainEventHandler.getEnumType(path, classType);
    }
}
