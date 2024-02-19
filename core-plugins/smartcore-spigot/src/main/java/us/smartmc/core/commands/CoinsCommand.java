package us.smartmc.core.commands;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;

public class CoinsCommand implements CommandExecutor {

    private void executeAdmin(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            SmartCorePlayer smartCorePlayer = SmartCorePlayer.get(args[0]);
            if (smartCorePlayer == null) {
                sender.sendMessage(ChatUtil.parse("&cNo target/player found with this name!"));
                return;
            }
            switch (args[1].toLowerCase()) {
                case "reset": {
                    smartCorePlayer.setCoins(0);
                    sender.sendMessage(ChatUtil.parse("&aBalance reset!"));
                    return;
                }
                case "add": {
                    if (args.length == 2) {
                        sender.sendMessage(ChatUtil.parse("&cCorrect usage: /coins <user> add <amount>"));
                        return;
                    }
                    try {
                        long amount = Long.parseLong(args[2]);
                        smartCorePlayer.addCoins(amount, "Administration");
                        sender.sendMessage(ChatUtil.parse("&aBalance added!"));
                        return;
                    } catch (Exception e) {
                        sender.sendMessage(ChatUtil.parse("&cError ocurred while trying to execute this command!"));
                    }
                    return;
                }
                case "remove": {
                    if (args.length == 2) {
                        sender.sendMessage(ChatUtil.parse("&cCorrect usage: /coins <user> remove <amount>"));
                        return;
                    }
                    try {
                        long amount = Long.parseLong(args[2]);
                        smartCorePlayer.removeCoins(amount, "Administration");
                        sender.sendMessage(ChatUtil.parse("&aBalance removed!"));
                        return;
                    } catch (Exception e) {
                        sender.sendMessage(ChatUtil.parse("&cError ocurred while trying to execute this command!"));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
            if (!sender.hasPermission("*")) {
                corePlayer.sendLanguageMessage("general", "noPermission");
                return true;
            }
        }
        if (!sender.hasPermission("*")) return true;
        executeAdmin(sender, args);
        return true;
    }
}
