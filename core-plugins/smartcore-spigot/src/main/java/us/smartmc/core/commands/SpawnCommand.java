package us.smartmc.core.commands;

import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.core.handler.SpawnHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = ((Player) sender).getPlayer();

        Location location = SpawnHandler.getLocation();
        if (location == null) {
            player.sendMessage(ChatUtil.parse(player, "&cSpawn is not set!"));
            return true;
        }
        player.teleport(location);
        return false;
    }
}
