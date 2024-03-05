package us.smartmc.event.eventscore.instance;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickHandler {

    @Getter
    private final InventoryClickEvent event;

    public ClickHandler(InventoryClickEvent event) {
        this.event = event;
    }

    public Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

}
