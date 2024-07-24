package us.smartmc.smartcore.proxy.instance.commandmanager;

import net.md_5.bungee.api.CommandSender;

public interface CustomCommandExecutor {

    void onCommand(CommandSender sender, String[] args);
}
