package us.smartmc.backend.service.players.common;

public class PlayersServiceContexts {

    public static String getUsernameContextId(String username) {
        return "player:" + username;
    }

}
