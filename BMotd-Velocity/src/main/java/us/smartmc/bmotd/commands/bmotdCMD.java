package us.smartmc.bmotd.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import us.smartmc.bmotd.BMotdVelocity;
import us.smartmc.bmotd.manager.MOTDManager;
import us.smartmc.bmotd.util.ChatUtil;

public class bmotdCMD implements SimpleCommand {

    private final BMotdVelocity plugin = BMotdVelocity.getPlugin();

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if(sender.hasPermission("*")){
            MOTDManager motdManager = plugin.getMotdManager();

            if(args[0].equalsIgnoreCase("reload")){
                BMotdVelocity.getPlugin().reloadBMotd();
                sender.sendMessage(Component.text("MOTD Reloaded!"));
            } else {
                sender.sendMessage(ChatUtil.parseToComponent(ChatUtil.color("Correct usage: /bmotd reload")));
            }
        } else {
            sender.sendMessage(ChatUtil.parseToComponent("&cPlugin BMotd vPRIVATE created by ImSergioh"));
        }
    }
}
