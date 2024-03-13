package us.smartmc.survivaladdon.fixescore.command;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ExecuteAtBungeeCommand extends BukkitCommand {

    public ExecuteAtBungeeCommand() {
        super("executeAtBungee");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) return true;
        Player player = Bukkit.getPlayer(args[0]);
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            cmdBuilder.append(args[i]).append(" ");
        }
        cmdBuilder.deleteCharAt(cmdBuilder.length() - 1);
        executeBungeeCordCommand(player.getName(), cmdBuilder.toString());
        return false;
    }

    private static void executeBungeeCordCommand(String name, String command) {
        if (command.startsWith("/")) command = command.replaceFirst("/", "");

        RedisConnection.mainConnection.getResource()
                .publish("PlayerChat",
                        name + "\n"
                                + command);
    }
}
