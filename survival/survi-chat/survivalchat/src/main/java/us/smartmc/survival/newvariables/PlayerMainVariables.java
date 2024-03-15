package us.smartmc.survival.newvariables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;

public class PlayerMainVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        if (!message.contains("<name>")) return message;
        message = message.replace("<name>", player.getName());
        return message;
    }
}
