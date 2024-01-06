package us.smartmc.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;

public class ExecuteAtBungeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("*")) return true;
        if (!(sender instanceof Player player)) return true;
        if (args.length == 0) return true;

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg + " ");
        }
        builder.deleteCharAt(builder.length() - 1);

        SmartCorePlayer.get(player).executeBungeeCordCommand(builder.toString());
        return false;
    }
}
