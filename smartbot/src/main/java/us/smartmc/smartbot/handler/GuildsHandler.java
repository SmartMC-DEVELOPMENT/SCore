package us.smartmc.smartbot.handler;

import java.util.*;

public class GuildsHandler {

    private static final Set<String> allowedGuilds = new HashSet<>();

    public static void register(String... ids) {
        for (String id : ids) {
            allowedGuilds.add(id);
            TicketsHandler.loadTicketsHandler(id);
        }
    }

    public static void unregister(String... ids) {
        Arrays.asList(ids).forEach(allowedGuilds::remove);
    }

    public static boolean isAllowed(String id) {
        return allowedGuilds.contains(id);
    }

}
