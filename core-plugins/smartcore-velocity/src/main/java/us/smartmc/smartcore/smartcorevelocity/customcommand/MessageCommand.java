package us.smartmc.smartcore.smartcorevelocity.customcommand;

import com.velocitypowered.api.command.CommandSource;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.instance.commandmanager.CustomCommandExecutor;

public class MessageCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i] + " ");
        }
        builder.deleteCharAt(builder.length() - 1);
        sender.sendMessage(Component.text(ChatUtil.parse(sender, builder.toString())));
    }
}
