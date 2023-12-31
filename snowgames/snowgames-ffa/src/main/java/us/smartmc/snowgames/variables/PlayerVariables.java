package us.smartmc.snowgames.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.player.FFAPlayer;

public class PlayerVariables extends VariableListener<Player> {
    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {

        FFAPlayer ffaPlayer = GamePlayerRepository.provide(FFAPlayer.class, player);
        if (ffaPlayer == null) return null;
        FFAGame game = FFAPlugin.getGame();

        message = VariableUtil.replace(message, "<map>", game.getMap().getName());

        int ticks = game.getMap().getMaxArenaTime();
        String formattedTime = formatTicksToTime(ticks);

        message = VariableUtil.replace(message, "<time_remaining>", formattedTime);

        message = VariableUtil.replace(message, "<kills>", String.valueOf(ffaPlayer.getKills()));
        message = VariableUtil.replace(message, "<deaths>", String.valueOf(ffaPlayer.getDeaths()));

        message = VariableUtil.replace(message, "<current_streak>", String.valueOf(ffaPlayer.getCurrentStreak()));
        message = VariableUtil.replace(message, "<max_streak>", String.valueOf(ffaPlayer.getMaxStreak()));

        return message;
    }

    private static String formatTicksToTime(int ticks) {
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
