package us.smartmc.core.luckywars.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.luckywars.instance.player.GamePlayer;

import java.util.UUID;

public class PlayersManager extends ManagerRegistry<UUID, GamePlayer> {

    @Override
    public void load() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            register(player.getUniqueId(), GamePlayer.get(player.getUniqueId()));
        }
    }

    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            unregister(player.getUniqueId());
        }
    }
}
