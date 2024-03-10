package us.smartmc.event.eventscore.handler;

import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.config.EventConfig;

import java.util.List;

public class MainEventHandler {

    public static <T extends Enum<T>> T getEnumType(String path, Class<T> tClass) {
        return EventsCore.getCore().getEventConfig().getEnumType(path, tClass);
    }

    public static <T extends Enum<T>> void setEnumType(String path, T value) {
        EventConfig config = EventsCore.getCore().getEventConfig();
        config.setEnumType(path, value);
        config.save();
    }

    public static <T extends Enum<T>> T getEnumType(String path, Class<T> tClass) {
        return config.getEnumType(path, tClass);
    }

    public static <T extends Enum<T>> void setEnumType(String path, T value) {
        config.setEnumType(path, value);
        config.save();
    }

    public static boolean isHoster(Player player) {
        if (player.hasPermission("*")) {
            return true;
        }
        return EventsCore.getCore().getEventConfig().getHoster().equals(player.getName());
    }

    public static boolean isParticipant(String name) {
        EventConfig config = EventsCore.getCore().getEventConfig();
        List<String> names = config.getParticipants();
        names.replaceAll(String::toLowerCase);
        return names.contains(name.toLowerCase());
    }

}
