package us.smartmc.survival.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.survival.util.VariableUtil;

public class PlayerMainVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        if (message == null) return null;
        message = VariableUtil.replace(message, "<name>", s -> player.getName());
        return message;
    }
}
