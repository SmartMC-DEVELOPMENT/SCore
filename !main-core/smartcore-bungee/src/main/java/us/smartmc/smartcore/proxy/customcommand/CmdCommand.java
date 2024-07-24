package us.smartmc.smartcore.proxy.customcommand;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.instance.commandmanager.CustomCommandExecutor;

public class CmdCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) return;
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        System.out.println("Executing " + player + " " + builder);
        BungeeCordPluginsAPI.proxy.getPluginManager().dispatchCommand(player, builder.toString());
    }
}
