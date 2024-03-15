package me.imsergioh.loginspigot.command;

import me.imsergioh.loginspigot.instance.LoginPlayer;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (!loginPlayer.isRegistered()) {
            loginPlayer.getPlayer().sendMessage("§cNo estás registrado! Registrate con /register!");
            return true;
        }
        if (args.length == 1) {
            loginPlayer.tryLogin(args[0]);
            return true;
        }
        player.sendMessage("§cUso correcto: /login <contraseña>");
        return false;
    }
}
