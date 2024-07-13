package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.util.VariableUtil;

public class ServerVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        message = VariableUtil.replace(message, "<serverId>", s -> SmartCore.getServerId());
        message = VariableUtil.replace(message, "<serverAlias>", s -> SmartCore.getServerAlias());
        message = VariableUtil.replace(message, "<serverNumber>", s -> String.valueOf(SmartCore.getServerNumber()));
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        return parse(message);
    }
}
