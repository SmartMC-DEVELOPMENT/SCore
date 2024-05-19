package us.smartmc.smartcore.smartcorevelocity.customcommand;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import us.smartmc.smartcore.smartcorevelocity.instance.commandmanager.CustomCommandExecutor;

public class CmdCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        if (!(sender instanceof Player player)) return;
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        System.out.println("Executing " + player + " " + builder.toString());
        VelocityPluginsAPI.proxy.getCommandManager().executeAsync(player, builder.toString());
    }
}
