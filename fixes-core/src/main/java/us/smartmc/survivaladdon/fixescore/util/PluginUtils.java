package us.smartmc.survivaladdon.fixescore.util;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.backend.request.ServerRedirectionRequest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PluginUtils {

    private static final Set<UUID> sendingPlayers = new HashSet<>();

    public static void redirectTo(Player player, String serverPrefix) {
        try {
            System.out.println("Redirecting " + player.getName() + " to " + serverPrefix);
            ServerRedirectionRequest request = new ServerRedirectionRequest(player.getUniqueId(), player.getName(), serverPrefix);
            request.send(RedisConnection.mainConnection);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error when trying to connect to a server of '" + serverPrefix + "'");
            throw new RuntimeException(e);
        }
    }

}
