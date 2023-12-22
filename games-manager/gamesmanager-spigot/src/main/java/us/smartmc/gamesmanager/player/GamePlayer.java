package us.smartmc.gamesmanager.player;

import lombok.Data;
import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.gamesmanager.player.annotations.GamePlayerData;
import us.smartmc.gamesmanager.player.annotations.MongoPlayerData;
import us.smartmc.gamesmanager.team.GameTeam;

import javax.annotation.Nullable;
import java.util.UUID;

@Data
public abstract class GamePlayer implements GamePlayerData {

    @MongoPlayerData
    protected final UUID uuid;
    protected final Player player;
    protected PlayerStatus status = PlayerStatus.LOBBY;

    @Nullable
    protected GameTeam team;

    protected GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;

        if (!player.isOnline()) this.setStatus(PlayerStatus.DISCONNECTED);
        this.load();
    }

    public void sendMessage(String message, Object... args) {
        player.sendMessage(ChatUtil.parse(message, args));
    }

    public void sendLanguageMessage(String holder, String path, Object... args) {
        CorePlayer.get(player).sendLanguageMessage(holder, path, args);
    }

    @Override
    public Object getId() {
        return this.getUuid();
    }
}
