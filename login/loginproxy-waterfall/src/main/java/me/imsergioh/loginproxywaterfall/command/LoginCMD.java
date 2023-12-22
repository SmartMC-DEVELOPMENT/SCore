package me.imsergioh.loginproxywaterfall.command;

import me.imsergioh.loginproxywaterfall.instance.CommandExecutor;
import me.imsergioh.loginproxywaterfall.instance.LoginPlayer;
import me.imsergioh.loginproxywaterfall.manager.LoginPlayersFactory;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LoginCMD implements CommandExecutor {

    @Override
    public void onCommand(ProxiedPlayer player, String[] args) {
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (!loginPlayer.isRegistered()) {
            loginPlayer.getPlayer().sendMessage("§cYou are not registered! Enter command /register!");
            return;
        }
        if (args.length == 1) {
            loginPlayer.tryLogin(args[0]);
            return;
        }
        player.sendMessage("§cCorrect usage: /login <password>");
    }
}
