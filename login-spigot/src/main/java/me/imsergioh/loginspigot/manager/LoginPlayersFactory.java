package me.imsergioh.loginspigot.manager;

import me.imsergioh.loginspigot.instance.LoginPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LoginPlayersFactory {

    private final static HashMap<UUID, LoginPlayer> players = new HashMap<>();

    public static LoginPlayer load(Player player) {
        LoginPlayer loginPlayer = players.get(player.getUniqueId());
        if (loginPlayer == null) {
            loginPlayer = new LoginPlayer(player.getUniqueId());
            players.put(player.getUniqueId(), loginPlayer);
        }
        return loginPlayer;
    }

    public static void unload(UUID uuid) {
        LoginPlayer lPlayer = players.get(uuid);
        if (lPlayer != null) {
            lPlayer.save();
        }
        players.remove(uuid);
    }

    public static LoginPlayer getTemporalLoginPlayer(UUID uuid) {
        if (players.containsKey(uuid)) return players.get(uuid);
        return new LoginPlayer(uuid);
    }

    public static LoginPlayer get(Player player) {
        if (!players.containsKey(player.getUniqueId())) return load(player);
        return players.get(player.getUniqueId());
    }
}
