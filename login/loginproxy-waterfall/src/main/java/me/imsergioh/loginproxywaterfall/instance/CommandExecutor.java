package me.imsergioh.loginproxywaterfall.instance;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface CommandExecutor {

    void onCommand(ProxiedPlayer player, String[] args);

}
