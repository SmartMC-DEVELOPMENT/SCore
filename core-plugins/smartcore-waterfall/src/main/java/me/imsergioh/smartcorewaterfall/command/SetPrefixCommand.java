package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SetPrefixCommand extends Command {

    public SetPrefixCommand() {
        super("setPrefix");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(ChatColor.RED + "Not permitted to execute this command.");
            return;
        }
        // USAGE: /setPrefix <rank> <color>
        if (args.length == 2) {
            String prefix = ("[" + args[0] + "]").toUpperCase();
            String command = "lpb group " + args[0] + " meta setprefix \"&" + args[1].replace("&", "") + prefix + "\"";
            SmartCoreWaterfall.getPlugin().getProxy().getPluginManager().dispatchCommand(
                    SmartCoreWaterfall.getPlugin().getProxy().getConsole(), command);
        } else {
            sender.sendMessage(ChatColor.RED + "Correct usage: /setPrefix <rankName> <color>");
        }
    }
}
