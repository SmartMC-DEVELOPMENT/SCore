package us.smartmc.core.commands;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.util.PluginUtils;

public class GameModeCommand implements CommandExecutor {

    private void player(Player player, String[] args) {
        if (args.length >= 2) {
            consoleOrWithArgs(player, args);
            return;
        }
        execute(player, player.getName(), args[0]);
    }

    private void consoleOrWithArgs(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(ChatUtil.parse("&cSe necesita de dos argumentos. Un jugador y un modo de juego"));
            return;
        }
        execute(sender, args[0], args[1]);
    }

    private void execute(CommandSender sender, String targetName, String gmArg) {
        Player player = Bukkit.getPlayer(targetName);
        if (player == null) {
            sender.sendMessage(ChatUtil.parse("&cNo se ha encontrado ese jugador."));
            return;
        }
        GameMode mode = PluginUtils.parseGameMode(gmArg);
        if (mode == null) {
            sender.sendMessage(ChatUtil.parse("&cEse modo de juego no es válido."));
            return;
        }
        player.setGameMode(mode);
        sender.sendMessage(successMessage(sender, targetName));
    }

    private String successMessage(CommandSender sender, String targetName) {
        if (sender.getName().equalsIgnoreCase(targetName)) {
            return ChatUtil.parse("&aHas cambiado tu modo de juego correctamente!");
        }
        return ChatUtil.parse("&aHas cambiado el modo de juego para &e" + targetName + "&a correctamente!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("smartcore.command.gamemode")) return true;

        if (args.length == 0) {
            sender.sendMessage(ChatUtil.parse("&cUso correcto: /gamemode <0, 1, 2, 3, s, c, a o e>"));
            return true;
        }

        if (sender instanceof Player) {
            player((Player) sender, args);
            return true;
        }
        consoleOrWithArgs(sender, args);
        return false;
    }
}