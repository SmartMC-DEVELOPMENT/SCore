package us.smartmc.event.eventscore.instance;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.handler.MainEventHandler;
import us.smartmc.event.eventscore.util.EnumUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerToggleableItem<T extends Enum<T>> extends GlobalToggleableItem<T> {

    private final Player player;

    public PlayerToggleableItem(Player player, String path, Class<T> classType) {
        super(path, classType);
        this.player = player;
    }

    @Override
    public T toggleType() {
        return null;
    }

    @Override
    public T getCurrentType() {
        return null;
    }
}
