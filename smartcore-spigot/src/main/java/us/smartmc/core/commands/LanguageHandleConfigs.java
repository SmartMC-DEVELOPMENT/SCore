package us.smartmc.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.menu.ReloadLanguageConfigMenu;

public class LanguageHandleConfigs implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        SmartCorePlayer corePlayer = SmartCorePlayer.get(((Player) sender).getPlayer());
        if (!sender.hasPermission("*")) {
            corePlayer.sendLanguageMessage("general", "noPermission");
            return true;
        }
        new ReloadLanguageConfigMenu(player).open(player);
        return true;
    }
}
