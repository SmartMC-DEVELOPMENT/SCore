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

        FFAPlayer ffaPlayer = FFAPlayerManager.INSTANCE.get(player.getUniqueId());
        if (ffaPlayer == null) return null;
        FFAGame game = FFAPlugin.getGame();
        if (game == null) return null;
        message = VariableUtil.replace(message, "<map>", game.getMap().getDisplayName());

        String formattedTime = formatSecondsToTime((int) (game.getMap().getEndTimestamp() - game.getMap().getStartTimestamp()));
        message = VariableUtil.replace(message, "<time_remaining>", formattedTime);

        message = VariableUtil.replace(message, "<kills>", String.valueOf(ffaPlayer.getKills()));
        message = VariableUtil.replace(message, "<deaths>", String.valueOf(ffaPlayer.getDeaths()));

        message = VariableUtil.replace(message, "<current_streak>", String.valueOf(ffaPlayer.getCurrentStreak()));
        message = VariableUtil.replace(message, "<max_streak>", String.valueOf(ffaPlayer.getMaxStreak()));

        return message;
    }

    private static String formatSecondsToTime(int secondsRemaining) {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
