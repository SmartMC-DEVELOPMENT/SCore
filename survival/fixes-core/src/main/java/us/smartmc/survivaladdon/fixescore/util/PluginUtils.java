package us.smartmc.survivaladdon.fixescore.util;

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
        if (sendingPlayers.contains(player.getUniqueId())) return;
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Redirecting " + player.getName() + " to " + serverPrefix);
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                out.writeUTF("RedirectTo");
                out.writeUTF(serverPrefix);
                player.sendPluginMessage(SmartAddonsSpigot.getPlugin(), "BungeeCord", b.toByteArray());
                b.close();
                out.close();
                sendingPlayers.remove(player.getUniqueId());
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Error when trying to connect to a server of '" + serverPrefix + "'");
                throw new RuntimeException(e);
            }
        });
    }

}
