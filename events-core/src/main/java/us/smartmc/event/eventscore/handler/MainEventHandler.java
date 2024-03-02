package us.smartmc.event.eventscore.handler;

import us.smartmc.event.eventscore.EventsCore;

public class MainEventHandler {

    private static final EventsCore core = EventsCore.getCore();

    public static boolean isHoster(String name) {
        return core.getEventConfig().getHoster().equals(name);
    }

    public static boolean isParticipant(String name) {

    }

}
