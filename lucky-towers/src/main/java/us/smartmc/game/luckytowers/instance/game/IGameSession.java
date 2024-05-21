package us.smartmc.game.luckytowers.instance.game;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

import java.util.Set;
import java.util.function.Consumer;

public interface IGameSession {

    void start();
    void end();

    boolean canStart();
    boolean canEnd();

    boolean canPlayersJoin(int amount);

    void joinPlayer(GamePlayer player);
    void quitPlayer(GamePlayer player);

    void deathPlayer(GamePlayer player);

    void setStatus(GameSessionStatus status);

    default void broadcastActionbar(IMessageCategory message, Object... args) {
        forEachOnlinePlayer(player -> {
            Language language = PlayerLanguages.get(player.getUniqueId());
            player.sendActionBar(PaperChatUtil.parse(player, message.getMessageOf(language), args));
        });
    }

    default void broadcastSound(Sound sound, float volume, float pitch) {
        forEachOnlinePlayer(player -> {
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

    default void broadcastMessage(IMessageCategory category, Object... args) {
        forEachOnlinePlayer(player -> {
            PaperChatUtil.send(player, category, args);
        });
    }

    default void broadcastMessage(String message, Object... args) {
        forEachOnlinePlayer(player -> {
            player.sendMessage(PaperChatUtil.parse(player, message, args));
        });
    }

    default void forEachOnlinePlayer(Consumer<Player> consumer) {
        getPlayers().forEach(gp -> gp.onlinePlayer(consumer));
    }

    default void forEachPlayer(Consumer<GamePlayer> consumer) {
        getPlayers().forEach(consumer);
    }

    GameSessionTeams getTeams();
    GameMap getMap();
    Set<GamePlayer> getAlivePlayers();
    Set<GamePlayer> getPlayers();

    GameSessionStatus getStatus();

    default GamePlayer gamePlayer(Player player) {
        return GamePlayer.get(player.getUniqueId());
    }
}
