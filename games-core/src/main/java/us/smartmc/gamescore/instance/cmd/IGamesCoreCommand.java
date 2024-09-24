package us.smartmc.gamescore.instance.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IGamesCoreCommand {

    boolean perform(CommandSender sender, String label, String[] args);

    default void performPlayer(Player player, String label, String[] args) {}

    default void performConsole(CommandSender sender, String label, String[] args) {}

}
