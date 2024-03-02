package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.instance.IToggleableItem;
import us.smartmc.event.eventscore.instance.IToggleablePlayerItem;
import us.smartmc.event.eventscore.instance.PlayerToggleableItem;
import us.smartmc.event.eventscore.instance.ToggleableItem;

import java.util.Arrays;
import java.util.List;

public abstract class EventCoreMenu extends CoreMenu {

    protected EventCoreMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    protected void set(int slot, Material material, String name, String descriptionLines, String command) {
        List<String> description = Arrays.asList(descriptionLines);
        description.replaceAll(s -> ChatUtil.color("&7" + s));
        ItemBuilder builder = ItemBuilder.of(material).name(name).lore(description);
        set(slot, builder.get());
    }

    protected  <T extends Enum<T>> void setToggleable(int slot, Class<T> tClass, String configPath) {
        ToggleableItem<T> item = new ToggleableItem<>(EventsCore.getCore().getEventConfig(), configPath, tClass);
        if (item.get() instanceof IToggleableItem<?>) {
            IToggleableItem<?> toggleableItem = (IToggleableItem<?>) item;
            set(slot, toggleableItem.getItem().get(), "toggleItem " + tClass.getName());
        }
    }

    protected <T extends Enum<T>> void setPlayerToggleable(int slot, Class<T> tClass) {
        PlayerToggleableItem<T> item = new PlayerToggleableItem<>(player, tClass);
        if (item.get(player) instanceof IToggleablePlayerItem<?>) {
            IToggleablePlayerItem<?> toggleableItem = (IToggleablePlayerItem<?>) item;
            set(slot, toggleableItem.getItem(player).get(player), "toggleItem " + tClass.getName());
        }
    }

}
