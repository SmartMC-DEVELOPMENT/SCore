package me.imsergioh.loginspigot.scheduler;

import me.imsergioh.loginspigot.LoginSpigot;
import me.imsergioh.loginspigot.instance.LoginPlayer;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class AuthAnnouncer {

    private static int taskId;

    public static void start() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(LoginSpigot.getPlugin(), () -> {
            for (LoginPlayer loginPlayer : getAllPlayersAtAuthServers()) {
                if (loginPlayer.isAuth()) continue;
                loginPlayer.sendAuthMessage();
            }
        }, 0, 20 * 3);
    }

    public static void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    private static Collection<LoginPlayer> getAllPlayersAtAuthServers() {
        Collection<LoginPlayer> result = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            result.add(LoginPlayersFactory.get(player));
        }
        return result;
    }
}
