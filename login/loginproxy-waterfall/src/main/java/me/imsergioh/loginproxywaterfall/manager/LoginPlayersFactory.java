package me.imsergioh.loginproxywaterfall.manager;

import me.imsergioh.loginproxywaterfall.instance.LoginPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class LoginPlayersFactory {

    private final static HashMap<UUID, LoginPlayer> players = new HashMap<>();

    public static void load(ProxiedPlayer player) {
        if (!players.containsKey(player.getUniqueId())) {
            players.put(player.getUniqueId(), new LoginPlayer(player.getUniqueId()));
        }
    }

    public static void unload(UUID uuid) {
        players.remove(uuid);
    }

    public static LoginPlayer get(ProxiedPlayer player) {
        return players.get(player.getUniqueId());
    }

}
