package me.imsergioh.loginproxywaterfall.scheduler;

import me.imsergioh.loginproxywaterfall.LoginProxyWaterfall;
import me.imsergioh.loginproxywaterfall.instance.LoginPlayer;
import me.imsergioh.loginproxywaterfall.manager.LoginPlayersFactory;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class AuthAnnouncer {

    private static ScheduledTask task;

    public static void start() {
        task = LoginProxyWaterfall.getPlugin().getProxy().getScheduler().schedule(LoginProxyWaterfall.getPlugin(), () ->{
            for (LoginPlayer loginPlayer : getAllPlayersAtAuthServers()) {
                if (loginPlayer.isAuth()) continue;
                loginPlayer.sendAuthMessage();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    public static void stop() {
        task.cancel();
    }

    private static Collection<LoginPlayer> getAllPlayersAtAuthServers() {
        Collection<LoginPlayer> result = new ArrayList<>();

        for (String name : LoginProxyWaterfall.getPlugin().getServers("auth")) {
            ServerInfo info = LoginProxyWaterfall.getPlugin().getProxy().getServerInfo(name);
            if (info == null) continue;
            for (ProxiedPlayer player : info.getPlayers()) {
                result.add(LoginPlayersFactory.get(player));
            }
        }
        return result;
    }
}
