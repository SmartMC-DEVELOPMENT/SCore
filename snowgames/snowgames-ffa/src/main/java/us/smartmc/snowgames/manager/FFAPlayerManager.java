package us.smartmc.snowgames.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import org.bukkit.Bukkit;
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
        Bukkit.getOnlinePlayers().forEach(player -> {
            unregister(player.getUniqueId());
        });
    }
}
