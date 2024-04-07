package us.smartmc.gamesmanager.gamesmanagerspigot.instance.player;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player.GamePlayerStatusChangeEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.util.BukkitUtil;

import java.util.UUID;

@Getter
public abstract class GamePlayer extends OfflineGamePlayer {

    @Getter
    private final Player player;

    @Setter
    private GameInstance gameInstance;

    private PlayerStatus status;

    public GamePlayer(GamePlayerManager<?> manager, UUID uuid) {
        super(manager, uuid);
        this.player = Bukkit.getPlayer(uuid);
    }

    public void sendMessage(String message, Object... args) {
        player.sendMessage(PaperChatUtil.parse(player, message, args));
    }

    public void sendTitle(String title, String subtitle, Object... args) {
        player.sendTitle(PaperChatUtil.parse(player, title, args),
                PaperChatUtil.parse(player, subtitle, args));
    }

    public void setStatus(PlayerStatus status) {
        PlayerStatus previous = this.status;
        GamePlayerStatusChangeEvent event = new GamePlayerStatusChangeEvent(this, previous, status);
        BukkitUtil.callEvent(event);
        if (event.isCancelled()) return;
        this.status = status;
    }
}
