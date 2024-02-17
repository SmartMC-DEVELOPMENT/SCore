package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.util.VariableUtil;

public class PlayerMainVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        if (message == null) return null;
        if (message.contains("<ping>")) {
            message = message.replace("<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping));
        }

        if (message.contains("<name>")) {
            message = message.replace("<name>", player.getName());
        }

        if (message.contains("<coins>")) {
            SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
            if (corePlayer != null) {
                message = VariableUtil.replace(message, "<coins>", corePlayer.getCoins() + "");
            }
        }

        return message;
    }
}
