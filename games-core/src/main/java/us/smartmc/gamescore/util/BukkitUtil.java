package us.smartmc.gamescore.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import us.smartmc.gamescore.api.GamesCoreAPI;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BukkitUtil {

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
        Bukkit.getPluginManager().callEvent(event);
    }
}
