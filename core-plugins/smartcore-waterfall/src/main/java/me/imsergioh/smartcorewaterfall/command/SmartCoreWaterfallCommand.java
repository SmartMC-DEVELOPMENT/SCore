package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.manager.CustomCommandsManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SmartCoreWaterfallCommand extends Command {

    public SmartCoreWaterfallCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(ChatUtil.parse("&bSmartCoreWaterfall plugin v " +
                    SmartCoreWaterfall.getPlugin().getDescription().getVersion() + " by ImSergioh!"));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage("Available Subcommands:\n" +
                    "reloadcustomcmds");
            return;
        }

        if (args[0].equalsIgnoreCase("reloadcustomcmds")) {
            CustomCommandsManager.forEach(customCommandsManager -> {
                customCommandsManager.load();
                sender.sendMessage(ChatUtil.parse("&aRecargado: &e" + customCommandsManager.getName()));
            });
            sender.sendMessage(ChatUtil.parse("&aRecargados!"));
        }

    }
}
