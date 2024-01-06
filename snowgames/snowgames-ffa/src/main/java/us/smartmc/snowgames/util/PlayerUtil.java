package us.smartmc.snowgames.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.listener.GameListeners;
import us.smartmc.snowgames.player.FFAPlayer;

import java.util.UUID;

import static us.smartmc.snowgames.listener.PlayerListeners.getAttacked;

public class PlayerUtil {

    public static void kill(Player victim) {
        FFAGame game = FFAPlugin.getGame();
        GamePlayer gamePlayer = GamePlayerRepository.provide(FFAPlayer.class, victim);
        if (gamePlayer == null) return;

        // Quit player from game
        game.quitPlayer(gamePlayer);

        FFAPlayer ffaVictim = GamePlayerRepository.provide(FFAPlayer.class, victim);
        if (ffaVictim == null) return;
        ffaVictim.addDeath();

        // Tries to get last player damage if not null then calls method addKillTo
        UUID attackerUUID = getAttacked.get(victim.getUniqueId());
        if (attackerUUID != null) {
            Player attackerPlayer = Bukkit.getPlayer(attackerUUID);
            addKillTo(attackerPlayer);
            announceKillMessage(victim, attackerPlayer);
        }
        // Remove last damage player from victim that has been killed
        getAttacked.remove(victim.getUniqueId());
    }

    public static void announceKillMessage(Player victim, Player killer) {

    }

    public static void addKillTo(Player player) {
        if (player == null) return;
        FFAPlayer killer = GamePlayerRepository.provide(FFAPlayer.class, player);
        if (killer == null) return;
        killer.addKill();
        GameItemUtils.handlePlayerKill(player);
    }

}
