package us.smartmc.core.luckywars.instance.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.luckywars.LuckyTowers;
import us.smartmc.core.luckywars.event.player.PlayerStatusChangeEvent;
import us.smartmc.core.luckywars.instance.game.GameSession;
import us.smartmc.core.luckywars.manager.PlayersManager;

import java.util.UUID;
import java.util.function.Consumer;

public class GamePlayer {

    private static final PlayersManager manager = LuckyTowers.getPlayersManager();

    @Getter
    private final UUID uuid;
    private Player bukkitPlayer;

    @Getter
    private final GamePlayerData data;

    @Setter @Getter
    private GameSession gameSession;

    private PlayerStatus status;

    private GamePlayer(UUID uuid) {
         this.uuid = uuid;
         this.data = loadData(uuid);
         this.bukkitPlayer = Bukkit.getPlayer(uuid);
    }

    public void setStatus(PlayerStatus status) {
        PlayerStatusChangeEvent event = new PlayerStatusChangeEvent(this, status, this.status);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        this.status = status;
    }

    public void onlinePlayer(Consumer<Player> consumer) {
        if (bukkitPlayer == null) bukkitPlayer = getBukkitPlayer();
        if (bukkitPlayer == null) return;
        consumer.accept(bukkitPlayer);
    }

    public Player getBukkitPlayer() {
        if (bukkitPlayer == null) {
            bukkitPlayer = Bukkit.getPlayer(uuid);
        }
        return bukkitPlayer;
    }

    private static GamePlayerData loadData(UUID uuid) {
        return new GamePlayerData(uuid);
    }

    public static GamePlayer get(UUID uuid) {
        GamePlayer gamePlayer = LuckyTowers.getPlayersManager().get(uuid);
        if (gamePlayer == null) {
            gamePlayer = new GamePlayer(uuid);
            manager.register(uuid, gamePlayer);
        }
        return gamePlayer;
    }

}
