package us.smartmc.game.luckytowers.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.util.GameUtil;

public class GameVariables extends VariableListener<Player> {

    @Override
    public String parse(String s) {
        return s;
    }

    @Override
    public String parse(Player player, String message) {
        VariableUtil.replace(message, "<countdown>", s -> GameUtil.getFormattedTimeFromSeconds(GamePlayer.get(player.getUniqueId()).getGameSession().getCountdown()));
        VariableUtil.replace(message, "<timeRemaining>", s -> GameUtil.getFormattedTimeFromSeconds(GamePlayer.get(player.getUniqueId()).getGameSession().getSecondsRemaining()));
        VariableUtil.replace(message, "<playersRemaining>", s -> String.valueOf(GamePlayer.get(player.getUniqueId()).getGameSession().getPlayersRemaining()));
        return message;
    }
}
