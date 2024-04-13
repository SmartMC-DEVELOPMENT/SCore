package us.smartmc.core.commands;

import us.smartmc.core.handler.SpawnHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("*")) return true;
        if (!(sender instanceof Player)) return true;
        Player player = ((Player) sender).getPlayer();

        SpawnHandler.set(player.getLocation());
        return false;
    }
}
