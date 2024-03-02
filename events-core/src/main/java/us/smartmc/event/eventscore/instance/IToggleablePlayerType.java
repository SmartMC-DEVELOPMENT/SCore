package us.smartmc.event.eventscore.instance;

import org.bukkit.entity.Player;

public interface IToggleablePlayerType<T extends Enum<T>> {

    T get(Player player);

    default void toggle(Player player) {
        T playerValue = get(player);
        T[] values = getEnums(getEnumClass());
        int length = values.length;
        int currentIndex = 0;
        while (!playerValue.equals(values[currentIndex])) {
            currentIndex++;
        }
        int nextIndex = currentIndex == length - 1 ? 0 : currentIndex + 1;
        T nextPlayerValue = values[nextIndex];
        set(player, nextPlayerValue);
    }

    void set(Player player, T value);

    Class<T> getEnumClass();

    static <T extends Enum<T>> T[] getEnums(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }
}
