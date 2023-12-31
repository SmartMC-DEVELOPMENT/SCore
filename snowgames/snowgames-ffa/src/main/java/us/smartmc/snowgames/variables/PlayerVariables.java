package us.smartmc.snowgames.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.player.FFAPlayer;

public class PlayerVariables extends VariableListener<FFAPlayer> {
    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(FFAPlayer ffaPlayer, String message) {

        Player player = ffaPlayer.getPlayer();
        FFAGame game = FFAPlugin.getGame();

        message = VariableUtil.replace(message, "<map>", game.getMap().getName());
        message = VariableUtil.replace(message, "<time_remaining>", String.valueOf(game.getMap().getMaxArenaTime()));

        message = VariableUtil.replace(message, "<kills>", String.valueOf(ffaPlayer.getKills()));
        message = VariableUtil.replace(message, "<deaths>", String.valueOf(ffaPlayer.getDeaths()));

        message = VariableUtil.replace(message, "<current_streak>", String.valueOf(ffaPlayer.getCurrentStreak()));
        message = VariableUtil.replace(message, "<max_streak>", String.valueOf(ffaPlayer.getMaxStreak()));
        
        return message;
    }
}
