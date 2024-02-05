package us.smartmc.snowgames.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.player.FFAPlayer;

import java.util.UUID;

public class FFAPlayerManager extends ManagerRegistry<UUID, FFAPlayer> {

    public static final FFAPlayerManager INSTANCE = new FFAPlayerManager();

    @Override
    public void load() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            register(player.getUniqueId(), new FFAPlayer(player));
        });
    }

    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            get(player.getUniqueId()).saveStats();
        }
    }
}
