package me.imsergioh.loginspigot;

import com.github.games647.craftapi.resolver.RateLimitException;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class LoginSpigotAuthPlugin implements AuthPlugin<Player> {

    @Override
    public boolean forceLogin(Player player) {
        boolean isPremium = false;
        try {
            isPremium = LoginSpigot.getFastLoginCore().getResolver().findProfile(player.getName()).isPresent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RateLimitException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Force login " + player.getName() + " " + player.getUniqueId() != null);
        return isPremium;
    }

    @Override
    public boolean forceRegister(Player player, String s) {
        boolean isPremium = false;
        try {
            isPremium = LoginSpigot.getFastLoginCore().getResolver().findProfile(player.getName()).isPresent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RateLimitException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Force register " + player.getName() + " " + player.getUniqueId() != null);
        return isPremium;
    }

    @Override
    public boolean isRegistered(String s) {
        UUID uuid = Bukkit.getOfflinePlayer(s).getUniqueId();
        System.out.println("Is registered " + s + " " + LoginPlayersFactory.getTemporalLoginPlayer(uuid).isRegistered());
        return LoginPlayersFactory.getTemporalLoginPlayer(uuid).isRegistered();
    }
}
