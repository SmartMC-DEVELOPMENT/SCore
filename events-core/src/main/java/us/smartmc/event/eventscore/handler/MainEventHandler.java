package us.smartmc.event.eventscore.handler;

import us.smartmc.event.eventscore.EventsCore;
import us.smartmc.event.eventscore.config.EventConfig;
import us.smartmc.event.eventscore.types.EventWhitelistType;

import java.util.List;

public class MainEventHandler {

    private static final EventsCore core = EventsCore.getCore();
    private static final EventConfig config = core.getEventConfig();

    public static void setWhitelistType(EventWhitelistType type) {
        config.set("");
    }

    public static boolean isHoster(String name) {
        return core.getEventConfig().getHoster().equals(name);
    }

    public static boolean isParticipant(String name) {
        List<String> names = core.getEventConfig().getParticipants();
        names.replaceAll(String::toLowerCase);
        return names.contains(name.toLowerCase());
    }

}
