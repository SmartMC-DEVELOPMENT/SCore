package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;

import java.util.HashMap;
import java.util.Map;

public abstract class EventCoreMenu extends CoreMenu {

    private final Map<Integer, Runnable> runnableActions = new HashMap<>();

    protected EventCoreMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    public void clickAction(int slot) {
        Runnable runnable = runnableActions.get(slot);
        if (runnable == null) return;
        runnable.run();
    }

    public void setToggleableItem(int slot, GlobalToggleableItem<?> toggleableItem) {
        set(slot, toggleableItem.getItem(), () -> {
            toggleableItem.toggleType();
            set(slot, toggleableItem.getItem());
        });
    }

    public void set(int slot, ItemStack itemStack, Runnable action) {
        set(slot, itemStack);
        runnableActions.put(slot, action);
    }
}
