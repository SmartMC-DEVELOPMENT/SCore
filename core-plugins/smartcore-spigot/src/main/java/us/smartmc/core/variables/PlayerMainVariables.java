package us.smartmc.core.variables;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import us.smartmc.core.pluginsapi.instance.VariableListener;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.util.VariableUtil;
import org.bukkit.entity.Player;

public class PlayerMainVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        message = VariableUtil.replace(message, "<name>", player.getName());
        message = VariableUtil.replace(message, "<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping));

        if (message.contains("<coins>")) {
            SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
            if (corePlayer != null) {
                message = VariableUtil.replace(message, "<coins>", corePlayer.getCoins() + "");
            }
        }

        return message;
    }
}
