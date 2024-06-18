package me.imsergioh.loginspigot.util;

import me.imsergioh.loginspigot.instance.LoginPlayer;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;

public class PluginUtils {

    public static void sendLoginRequest(Player player) {
        try {
            System.out.println("Sending login request! " + player.getName());
            RedisConnection.mainConnection.getResource().publish("login", player.getUniqueId().toString());
        } catch (Exception e) {
            System.out.println("Sending login request failed! " + player.getName());
            player.sendMessage(ChatColor.RED + "Error when trying to login " + player.getName());
            throw new RuntimeException(e);
        }
    }

}
