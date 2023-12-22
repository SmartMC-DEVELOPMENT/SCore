package us.smartmc.core.pluginsapi.spigot.item;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickHandler {

    private final Player clicker;
    private final ItemStack item;
    private final Event event;

    public ClickHandler(Player player, ItemStack item, Event event) {
        this.clicker = player;
        this.event = event;
        this.item = item;
    }

    public InventoryClickEvent clickEvent() {
        return (InventoryClickEvent) event;
    }

    public PlayerInteractEvent interactEvent() {
        return (PlayerInteractEvent) event;
    }

    public ClickType clickType() {
        return clickEvent().getClick();
    }

    public Action interactAction() {
        return interactEvent().getAction();
    }

    public InventoryAction getInventoryAction() {
        return clickEvent().getAction();
    }

    public Player getClicker() {
        return clicker;
    }

    public Player clicker() {
        return clicker;
    }

    public ItemStack item() {
        return item;
    }

    public ItemStack getItem() {
        return item;
    }

    public Player getPlayer() {
        return clicker;
    }

    public Player player() {
        return clicker;
    }
}
