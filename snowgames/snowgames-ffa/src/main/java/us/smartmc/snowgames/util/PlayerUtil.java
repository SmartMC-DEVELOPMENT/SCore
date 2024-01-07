package us.smartmc.snowgames.util;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.game.GameSession;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.manager.DeathMessagesManager;
import us.smartmc.snowgames.manager.KillMessagesManager;
import us.smartmc.snowgames.manager.ListMessagesManager;
import us.smartmc.snowgames.player.FFAPlayer;

import java.util.UUID;

import static us.smartmc.snowgames.listener.PlayerListeners.getAttacked;

public class PlayerUtil {

    public static void kill(Player victim) {
        DebugUtil.debug(PlayerUtil.class.getSimpleName(), "kill start");
        FFAGame game = FFAPlugin.getGame();
        FFAPlayer ffaPlayer = GamePlayerRepository.provide(FFAPlayer.class, victim);
        if (ffaPlayer == null) return;

        // Quit player from game
        game.quitPlayer(ffaPlayer);

        FFAPlayer ffaVictim = GamePlayerRepository.provide(FFAPlayer.class, victim);
        if (ffaVictim == null) return;
        ffaVictim.addDeath();

        // Tries to get last player damage if not null then calls method addKillTo
        UUID attackerUUID = getAttacked.get(victim.getUniqueId());
        if (attackerUUID != null) {
            Player attackerPlayer = Bukkit.getPlayer(attackerUUID);
            addKillTo(attackerPlayer);
            announceListMessageManager(KillMessagesManager.INSTANCE, game, victim, attackerPlayer);
        } else {
            announceListMessageManager(DeathMessagesManager.INSTANCE, game, victim, null);
        }
        // Remove last damage player from victim that has been killed
        getAttacked.remove(victim.getUniqueId());
        DebugUtil.debug(PlayerUtil.class.getName(), "kill end");
    }

    public static void announceListMessageManager(
            ListMessagesManager manager,
            FFAGame game,
            Player victim,
            Player killer) {
        int messageIndex = manager.getRandomMessageIndex();
        game.forEachGamePlayer(FFAPlayer.class, player -> {
            Language language = PlayerLanguages.get(player.getUUID());

            String message = killer == null ?
                    ChatUtil.parse(player.getPlayer(), manager.getRandomMessageFromList(messageIndex, language),
                    victim.getName()) :
                    ChatUtil.parse(player.getPlayer(), manager.getRandomMessageFromList(messageIndex, language),
                    victim.getName(), killer.getName());
            player.sendMessage(message);
        });
    }

    public static void addKillTo(Player player) {
        if (player == null) return;
        FFAPlayer killer = GamePlayerRepository.provide(FFAPlayer.class, player);
        if (killer == null) return;
        killer.addKill();
        GameItemUtils.handlePlayerKill(player);
    }

}
