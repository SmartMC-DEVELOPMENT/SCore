package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;

public class ServerVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        if (message.equals("<serverId>")) {
            return SmartCore.getServerID();
        }
        if (message.equals("<serverAlias>")) {
            return SmartCore.getServerAlias();
        }
        if (message.equals("<serverNumber>")) {
            return String.valueOf(SmartCore.getServerNumber());
        }
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        return parse(message);
    }
}
