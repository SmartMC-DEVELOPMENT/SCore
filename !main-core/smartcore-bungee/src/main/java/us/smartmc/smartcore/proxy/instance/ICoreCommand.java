package us.smartmc.smartcore.proxy.instance;

import net.md_5.bungee.api.CommandSender;

public interface ICoreCommand {

    void onCommand(CommandSender sender, String[] args);

    String getName();

}
