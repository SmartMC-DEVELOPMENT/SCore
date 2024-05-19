package me.imsergioh.loginspigot.command;

import me.imsergioh.loginspigot.instance.LoginPlayer;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (loginPlayer.isRegistered()) {
            player.sendMessage("§cYou are already registered!");
            return true;
        }
        if (args.length == 2) {
            loginPlayer.register(args[0], args[1]);
            return true;
        }
        player.sendMessage("§cUso correcto: /register <contraseña> <confirmar contraseña>");
        return true;
    }
}
