package us.smartmc.core.commands;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.handler.SpawnHandler;

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
