package us.smartmc.gamescore.util;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.api.GamesCoreAPI;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BukkitUtil {

    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent chat = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void consumePlayer(UUID uuid, Consumer<Player> consumer) {
        getPlayer(uuid).ifPresent(consumer);
    }

    public static Optional<Player> getPlayer(UUID uuid) {
        if (!isBukkitPlatformAvailable()) return Optional.empty();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return Optional.empty();
        return Optional.of(player);
    }

    public static Optional<Player> getPlayer(String name) {
        if (!isBukkitPlatformAvailable()) return Optional.empty();
        return Optional.of(Bukkit.getPlayer(name));
    }

    public static boolean isBukkitPlatformAvailable() {
        return Bukkit.getServer() != null;
    }

    public static void runLater(Runnable runnable, long ticksDelay) {
        if (!isBukkitPlatformAvailable()) {
            long millis = ticksDelay * 50;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, millis);
            return;
        }
        Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, ticksDelay);
    }

    public static void runSync(Runnable runnable) {
        if (!isBukkitPlatformAvailable()) {
            runnable.run();
            return;
        }
        Bukkit.getScheduler().runTask(GamesCoreAPI.getApi().getPlugin(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        if (!isBukkitPlatformAvailable()) {
            CompletableFuture.runAsync(runnable);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(GamesCoreAPI.getApi().getPlugin(), runnable);
    }

    public static void callEvent(Event event) {
        runSync(() -> Bukkit.getPluginManager().callEvent(event));
    }

    private static JavaPlugin getPlugin() {
        return GamesCoreAPI.getApi().getPlugin();
    }
}
