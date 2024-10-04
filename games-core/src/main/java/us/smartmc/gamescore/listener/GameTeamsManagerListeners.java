
package us.smartmc.gamescore.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.GameStatus;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.team.GenericGameTeamsManager;

public class GameTeamsManagerListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayersDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player entity) {
            Game game = GameCorePlayer.of(damager).getCurrentGame();
            if (game != null && !game.getStatus().equals(GameStatus.PLAYING)) return;
            boolean areInSameTeam = areAtSameTeam(damager, entity);
            if (isAllyDamageAllowed()) {
                if (areInSameTeam) {
                    event.setCancelled(false);
                    return;
                }
            } else {
                // Not allowed damage friends
                if (areInSameTeam) {
                    event.setCancelled(true);
                    return;
                }
                event.setCancelled(false);
            }
        }
    }

    private static GameTeam getTeam(Player player) {
        GameCorePlayer gamePlayer = GameCorePlayer.of(player);
        if (gamePlayer == null) return null;
        if (gamePlayer.getCurrentGame() == null) return null;
        return gamePlayer.getCurrentGame().getGameSessionTeamsManager().getGameTeam(player);
    }

    private static boolean areAtSameTeam(Player player1, Player player2) {
        if (!areAtSameGame(player1, player2)) return false;
        GameTeam team1 = getTeam(player1);
        GameTeam team2 = getTeam(player2);
        if (team1 == null || team2 == null) return false;
        return team1.equals(team2);
    }

    private static boolean areAtSameGame(Player player1, Player player2) {
        Game game1 = GameCorePlayer.of(player1).getCurrentGame();
        Game game2 = GameCorePlayer.of(player2).getCurrentGame();
        if (game1 == null || game2 == null) return false;
        return game1.equals(game2);
    }

    private static boolean isAllyDamageAllowed() {
        GenericGameTeamsManager manager = getGenericGameTeamsManager();
        if (manager == null) return false;
        return manager.isAllyDamageAllowed();
    }

    private static GenericGameTeamsManager getGenericGameTeamsManager() {
        return MapManager.getManager(GenericGameTeamsManager.class);
    }
}
