package us.smartmc.smartcore.proxy.customcommand;

import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.instance.commandmanager.CustomCommandExecutor;

public class MessageCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        sender.sendMessage(ChatUtil.parse(player, builder.toString()));
    }
}
