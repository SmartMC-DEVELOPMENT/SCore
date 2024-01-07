package us.smartmc.gamesmanager.player;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.player.annotations.GamePlayerData;
import us.smartmc.gamesmanager.player.annotations.MongoPlayerData;
import us.smartmc.gamesmanager.team.GameTeam;

import javax.annotation.Nullable;
import java.util.UUID;

@Getter @Setter
public abstract class GamePlayer extends CorePlayer implements GamePlayerData {

    @MongoPlayerData
    protected final UUID uuid;
    protected final Player player;
    protected PlayerStatus status = PlayerStatus.LOBBY;

    @Nullable
    protected GameTeam team;

    protected GamePlayer(Player player) {
        super(player.getUniqueId());
        this.uuid = player.getUniqueId();
        this.player = player;

        if (!player.isOnline()) setStatus(PlayerStatus.DISCONNECTED);
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
        return getUuid();
    }
}
