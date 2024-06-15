package us.smartmc.smartcore.smartcorevelocity.instance.player;

import com.velocitypowered.api.proxy.Player;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.smartcore.smartcorevelocity.backend.service.PlayersService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SmartCoreProxyPlayer {

    private static final Map<UUID, SmartCoreProxyPlayer> players = new HashMap<>();

    private final Player player;

    public SmartCoreProxyPlayer(Player player) {
        this.player = player;
        players.put(player.getUniqueId(), this);
    }

    public void load() {
        ServicesManager.get(PlayersService.class).registerPlayerContext(player);
    }

    public void unload() {
        ServicesManager.get(PlayersService.class).unregisterPlayerContext(player);
    }

    public static boolean alreadyRegistered(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public static SmartCoreProxyPlayer get(Player player) {
        return players.getOrDefault(player.getUniqueId(), new SmartCoreProxyPlayer(player));
    }

}
