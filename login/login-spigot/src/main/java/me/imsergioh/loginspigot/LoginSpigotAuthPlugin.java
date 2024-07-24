package me.imsergioh.loginspigot;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class LoginSpigotAuthPlugin implements AuthPlugin<Player> {

    @Override
    public boolean forceLogin(Player player) {
        return FastLoginBukkit.getPlugin(FastLoginBukkit.class).getCore().getStorage().loadProfile(player.getName()).isPremium();
    }

    @Override
    public boolean forceRegister(Player player, String s) {
        return FastLoginBukkit.getPlugin(FastLoginBukkit.class).getCore().getStorage().loadProfile(player.getName()).isPremium();
    }

    @Override
    public boolean isRegistered(String s) {
        UUID uuid = Bukkit.getOfflinePlayer(s).getUniqueId();
        return LoginPlayersFactory.getTemporalLoginPlayer(uuid).isRegistered();
    }
}
