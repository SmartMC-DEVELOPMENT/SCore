package us.smartmc.smartcore.proxy.instance.player;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.smartcore.proxy.backend.service.PlayersService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SmartCoreProxyPlayer {

    private static final Map<UUID, SmartCoreProxyPlayer> players = new HashMap<>();

    private final ProxiedPlayer player;

    public SmartCoreProxyPlayer(ProxiedPlayer player) {
        this.player = player;
        players.put(player.getUniqueId(), this);
    }

    public void load() {
        ServicesManager.get(PlayersService.class).registerPlayerContext(player);
    }

    public void unload() {
        ServicesManager.get(PlayersService.class).unregisterPlayerContext(player);
    }

    public static boolean alreadyRegistered(ProxiedPlayer player) {
        return players.containsKey(player.getUniqueId());
    }

    public static SmartCoreProxyPlayer get(ProxiedPlayer player) {
        return players.getOrDefault(player.getUniqueId(), new SmartCoreProxyPlayer(player));
    }

}
