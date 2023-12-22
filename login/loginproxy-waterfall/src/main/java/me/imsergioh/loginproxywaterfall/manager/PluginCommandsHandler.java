package me.imsergioh.loginproxywaterfall.manager;

import me.imsergioh.loginproxywaterfall.instance.CommandExecutor;
import me.imsergioh.loginproxywaterfall.instance.LoginPlayer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.HashMap;

public class PluginCommandsHandler implements Listener {

    private static final HashMap<String, CommandExecutor> executors = new HashMap<>();

    public static void register(String name, CommandExecutor executor) {
        executors.put(name, executor);
    }

    @EventHandler(priority = 1)
    public void chat(ChatEvent event) {
        Connection connection = event.getSender();
        if (!(connection instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) connection;
        perform(event, player);
    }

    private static void perform(ChatEvent event, ProxiedPlayer player) {
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (loginPlayer.isAuth()) return;
        String cmdName = event.getMessage().replaceFirst("/", "").split(" ")[0];
        String[] args = event.getMessage().replaceFirst("/" + cmdName + " ", "").split(" ");
        if (!executors.containsKey(cmdName)) return;
        executors.get(cmdName).onCommand(player, args);
        event.setCancelled(true);
    }

}
