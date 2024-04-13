package me.imsergioh.loginspigot;

import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LoginSpigotAuthPlugin implements AuthPlugin<Player> {

    @Override
    public boolean forceLogin(Player player) {
        UUID uuid = LoginSpigot.getFastLoginCore().getApiConnector().getPremiumUUID(player.getName());
        LoginPlayersFactory.get(player).forceLogin();
        System.out.println("Force login " + player.getName() + " " + uuid != null);
        return uuid != null;
    }

    @Override
    public boolean forceRegister(Player player, String s) {
        UUID uuid = LoginSpigot.getFastLoginCore().getApiConnector().getPremiumUUID(s);
        System.out.println("Force register " + player.getName() + " " + uuid != null);
        return uuid != null;
    }

    @Override
    public boolean isRegistered(String s) {
        UUID uuid = Bukkit.getOfflinePlayer(s).getUniqueId();
        System.out.println("Is registered " + s + " " + LoginPlayersFactory.getTemporalLoginPlayer(uuid).isRegistered());
        return LoginPlayersFactory.getTemporalLoginPlayer(uuid).isRegistered();
    }
}
