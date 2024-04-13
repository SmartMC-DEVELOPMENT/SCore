package us.smartmc.core.util;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PluginUtils {

    private static final Set<UUID> sendingPlayers = new HashSet<>();

    public static boolean isSendingPlayer(Player player) {
        return sendingPlayers.contains(player.getUniqueId());
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
