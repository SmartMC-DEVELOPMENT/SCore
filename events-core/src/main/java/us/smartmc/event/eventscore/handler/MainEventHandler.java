package us.smartmc.event.eventscore.handler;

import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.config.EventConfig;

import java.util.List;

public class MainEventHandler {

    private static final EventsCore core = EventsCore.getCore();
    private static final EventConfig config = core.getEventConfig();

    public static boolean isHoster(Player player) {
        if (player.hasPermission("*")) {
            return true;
        }
        return core.getEventConfig().getHoster().equals(player.getName());
    }

    public static boolean isParticipant(String name) {
        List<String> names = core.getEventConfig().getParticipants();
        names.replaceAll(String::toLowerCase);
        return names.contains(name.toLowerCase());
    }

}
