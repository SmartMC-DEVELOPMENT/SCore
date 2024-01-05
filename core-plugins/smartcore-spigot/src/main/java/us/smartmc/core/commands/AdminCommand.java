package us.smartmc.core.commands;

import org.bukkit.ChatColor;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.AdminModeHandler;
import us.smartmc.core.instance.player.SmartCorePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static us.smartmc.core.util.ItemUtils.wand;

public class AdminCommand implements CommandExecutor {

    private static final AdminModeHandler handler = SmartCore.getPlugin().getAdminModeHandler();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        SmartCorePlayer corePlayer = SmartCorePlayer.get(((Player) sender).getPlayer());
        if (!sender.hasPermission("*")) {
            corePlayer.sendLanguageMessage("general", "noPermission");
            return true;
        }
        if(args.length > 0 && args[0].equalsIgnoreCase("wand")) {
            player.getInventory().addItem(wand);
            player.sendMessage(ChatColor.AQUA+"Added region wand to your inventory!");
        }
        handler.toggle(corePlayer.player());
        return true;
    }
}
