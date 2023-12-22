package us.smartmc.core.util;

import org.bukkit.*;
import us.smartmc.core.SmartCore;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class PluginUtils {

    public static void sendTo(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(SmartCore.getPlugin(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error when trying to connect to " + server);
        }
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
