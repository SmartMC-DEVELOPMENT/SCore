package us.smartmc.smartcore.smartcorevelocity.customcommand;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import us.smartmc.smartcore.smartcorevelocity.instance.commandmanager.CustomCommandExecutor;

public class MessageCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        if (!(sender instanceof Player player)) return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        sender.sendMessage(VelocityChatUtil.parseToComponent(player, builder.toString()));
    }
}
