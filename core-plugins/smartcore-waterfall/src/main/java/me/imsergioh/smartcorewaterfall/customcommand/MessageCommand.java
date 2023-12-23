package me.imsergioh.smartcorewaterfall.customcommand;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.instance.commandmanager.CustomCommandExecutor;
import net.md_5.bungee.api.CommandSender;

public class MessageCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i] + " ");
        }
        builder.deleteCharAt(builder.length() - 1);
        sender.sendMessage(ChatUtil.parse(sender, builder.toString()));
    }
}
