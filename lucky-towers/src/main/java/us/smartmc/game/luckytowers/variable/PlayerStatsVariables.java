package us.smartmc.game.luckytowers.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.GamePlayerData;

public class PlayerStatsVariables extends VariableListener<Player> {

    @Override
    public String parse(String s) {
        return s;
    }

    @Override
    public String parse(Player player, String message) {
        //VariableUtil.replace(message, "<countdown>", s -> );
        return message;
    }
}
