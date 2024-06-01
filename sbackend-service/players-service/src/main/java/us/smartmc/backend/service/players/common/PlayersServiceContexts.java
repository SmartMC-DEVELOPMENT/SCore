package us.smartmc.backend.service.players.common;

import java.util.UUID;

public class PlayersServiceContexts {

    public static String getPlayerContext(UUID id) {
        return "mcPlayer@" + id;
    }

}
