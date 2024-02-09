package us.smartmc.gamesmanager.player;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.team.GameTeam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GamePlayer {

    private static final Map<UUID, GamePlayer> cache = new HashMap<>();

    private final Player bukkitPlayer;

    @Setter
    private PlayerStatus status;

    @Setter
    private GameTeam gameTeam;

    public GamePlayer(Player player) {
        this.bukkitPlayer = player;
    }

    public void sendMessage(String message, Object... args) {
        bukkitPlayer.sendMessage(ChatUtil.parse(bukkitPlayer, message, args));
    }

    public static GamePlayer get(Player player) {
        return cache.computeIfAbsent(player.getUniqueId(), k -> new GamePlayer(player));
    }

}
