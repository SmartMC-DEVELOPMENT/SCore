package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.event.eventscore.instance.ClickHandler;
import us.smartmc.event.eventscore.instance.GlobalToggleableItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class EventCoreMenu extends CoreMenu {

    protected final Map<Integer, Consumer<ClickHandler>> clickActions = new HashMap<>();

    protected EventCoreMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    public void clickAction(ClickHandler handler) {
        Consumer<ClickHandler> consumer = clickActions.get(handler.getEvent().getSlot());
        if (consumer == null) return;
        consumer.accept(handler);
    }

    public void set(int slot, ItemStack itemStack, Consumer<ClickHandler> action) {
        set(slot, itemStack);
        clickActions.put(slot, action);
    }
}
