package me.imsergioh.loginproxywaterfall.command;

import me.imsergioh.loginproxywaterfall.instance.CommandExecutor;
import me.imsergioh.loginproxywaterfall.instance.LoginPlayer;
import me.imsergioh.loginproxywaterfall.manager.LoginPlayersFactory;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RegisterCMD implements CommandExecutor {

    @Override
    public void onCommand(ProxiedPlayer player, String[] args) {
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (loginPlayer.isRegistered()) {
            player.sendMessage("§cYou are already registered!");
            return;
        }
        if (args.length == 2) {
            loginPlayer.register(args[0], args[1]);
            return;
        }
        player.sendMessage("§cCorrect usage: /register <password> <confirm password>");
    }
}
