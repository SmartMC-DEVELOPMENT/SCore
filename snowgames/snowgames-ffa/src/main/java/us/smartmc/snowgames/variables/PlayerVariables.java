package us.smartmc.snowgames.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.manager.FFAPlayerManager;
import us.smartmc.snowgames.player.FFAPlayer;

public class PlayerVariables extends VariableListener<Player> {
    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {

        message = VariableUtil.replace(message, "<map>", s -> ffaGame().getMap().getDisplayName());

        message = VariableUtil.replace(message, "<time_remaining>", s -> formatSecondsToTime((int) (ffaGame().getMap().getEndTimestamp() - System.currentTimeMillis() / 1000)));

        message = VariableUtil.replace(message, "<kills>", s -> String.valueOf(ffaPlayer(player).getKills()));
        message = VariableUtil.replace(message, "<deaths>", s -> String.valueOf(ffaPlayer(player).getDeaths()));

        message = VariableUtil.replace(message, "<current_streak>", s -> String.valueOf(ffaPlayer(player).getCurrentStreak()));
        message = VariableUtil.replace(message, "<max_streak>", s -> String.valueOf(ffaPlayer(player).getMaxStreak()));

        return message;
    }

    public FFAGame ffaGame() {
        return FFAPlugin.getGame();
    }

    public FFAPlayer ffaPlayer(Player player) {
        return FFAPlugin.getFFAPlugin().getGamePlayerManager().get(player.getUniqueId());
    }

    private static String formatSecondsToTime(int secondsRemaining) {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
