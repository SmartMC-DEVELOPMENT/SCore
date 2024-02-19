package us.smartmc.core.util;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PluginUtils {

    private static final Set<UUID> sendingPlayers = new HashSet<>();

    public static void sendTo(Player player, String serverPrefix) {
        if (sendingPlayers.contains(player.getUniqueId())) return;
        CompletableFuture.runAsync(() -> {
            sendingPlayers.add(player.getUniqueId());
            // Save before redirect to server ->
            CorePlayer corePlayer = CorePlayer.get(player);
            if (corePlayer == null) return;
            CorePlayerData playerData = corePlayer.getPlayerData();
            if (playerData == null) return;
            playerData.save();
        }).thenRun(() -> {
            try {
                System.out.println("Redirecting " + player.getName() + " to " + serverPrefix);
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                out.writeUTF("RedirectTo");
                out.writeUTF(serverPrefix);
                player.sendPluginMessage(SmartCore.getPlugin(), "BungeeCord", b.toByteArray());
                b.close();
                out.close();
                sendingPlayers.remove(player.getUniqueId());
            } catch (Exception e) {
                e.printStackTrace(System.out);
                player.sendMessage(ChatColor.RED + "Error when trying to connect to a server of '" + serverPrefix + "'");
            }
        });
    }

    public static World getOrLoadWorld(String name) {
        return Bukkit.getServer().createWorld(new WorldCreator(name));
    }

    public static GameMode parseGameMode(String text) {
        text = text.toLowerCase();
        if (text.startsWith("su") || text.equals("s") || text.equals("0")) return GameMode.SURVIVAL;
        if (text.startsWith("c") || text.equals("1")) return GameMode.CREATIVE;
        if (text.startsWith("a") || text.equals("2")) return GameMode.ADVENTURE;
        if (text.startsWith("e") || text.startsWith("sp") || text.equals("3")) return GameMode.SPECTATOR;
        return null;
    }

}
