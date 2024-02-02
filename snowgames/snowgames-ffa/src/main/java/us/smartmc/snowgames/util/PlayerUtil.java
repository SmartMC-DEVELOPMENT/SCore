package us.smartmc.snowgames.util;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.game.FFAMap;
import us.smartmc.snowgames.manager.DeathMessagesManager;
import us.smartmc.snowgames.manager.FFAPlayerManager;
import us.smartmc.snowgames.manager.KillMessagesManager;
import us.smartmc.snowgames.manager.ListMessagesManager;
import us.smartmc.snowgames.player.FFAPlayer;

import java.util.UUID;

import static us.smartmc.snowgames.listener.PlayerListeners.getAttacked;

public class PlayerUtil {

    public static void kill(Player victim) {
        DebugUtil.debug(PlayerUtil.class.getSimpleName(), "kill start");
        FFAGame game = FFAPlugin.getGame();
        FFAPlayer ffaVictim = FFAPlayerManager.INSTANCE.get(victim.getUniqueId());
        if (ffaVictim == null) return;

        // Quit player from game
        game.quitPlayer(ffaVictim.getPlayer());

        ffaVictim.addDeath();

        FFAMap map = FFAPlugin.getPlugin().getArenaManager().getCurrentMap();
        ffaVictim.getPlayer().teleport(map.getSpawn());

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
        game.forEachPlayer(player -> {
            Language language = PlayerLanguages.get(player.getUniqueId());

            String message = killer == null ?
                    ChatUtil.parse(player.getPlayer(), manager.getRandomMessageFromList(messageIndex, language),
                    victim.getName()) :
                    ChatUtil.parse(player.getPlayer(), manager.getRandomMessageFromList(messageIndex, language),
                    victim.getName(), killer.getName());
            player.getPlayer().sendMessage(message);
        });
    }

    public static void addKillTo(Player player) {
        if (player == null) return;
        FFAPlayer killer = FFAPlayerManager.INSTANCE.get(player.getUniqueId());
        if (killer == null) return;
        killer.addKill();
        GameItemUtils.handlePlayerKill(player);
    }

}
