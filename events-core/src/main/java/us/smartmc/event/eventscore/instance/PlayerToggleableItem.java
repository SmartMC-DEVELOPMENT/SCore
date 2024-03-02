package us.smartmc.event.eventscore.instance;

import org.bukkit.entity.Player;

public class PlayerToggleableItem<T extends Enum<T>> implements IToggleablePlayerType<T> {

    private final Player player;
    private final Class<T> tClass;

    public PlayerToggleableItem(Player player, Class<T> tClass) {
        this.player = player;
        this.tClass = tClass;
    }

    @Override
    public T get(Player player) {
        return null;
    }

    @Override
    public void set(Player player, T value) {

    }

    @Override
    public Class<T> getEnumClass() {
        return tClass;
    }
}
