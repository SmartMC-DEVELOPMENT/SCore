package me.imsergioh.loginspigot.util;

import me.imsergioh.loginspigot.LoginSpigot;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PluginUtils {

    private static final Set<UUID> sendingPlayers = new HashSet<>();

    public static void redirectTo(Player player, String serverPrefix) {
        if (sendingPlayers.contains(player.getUniqueId())) return;
        try {
            System.out.println("Redirecting " + player.getName() + " to " + serverPrefix);
            sendingPlayers.add(player.getUniqueId());
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("RedirectTo");
            out.writeUTF(serverPrefix);
            player.sendPluginMessage(LoginSpigot.getPlugin(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
            sendingPlayers.remove(player.getUniqueId());
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error when trying to connect to a server of '" + serverPrefix + "'");
            throw new RuntimeException(e);
        }
    }

}
