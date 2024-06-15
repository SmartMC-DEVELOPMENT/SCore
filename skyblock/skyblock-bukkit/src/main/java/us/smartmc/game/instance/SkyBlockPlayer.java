package us.smartmc.game.instance;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.skyblock.instance.player.ISkyBlockPlayer;
import us.smartmc.skyblock.manager.PlayersManager;

import java.util.UUID;

@Getter
public class SkyBlockPlayer implements ISkyBlockPlayer {

    private final UUID id;

    private SkyBlockPlayerData playerData;

    protected SkyBlockPlayer(UUID id) {
        this.id = id;
        PlayersManager.register(this);
        Bukkit.getScheduler().runTaskAsynchronously(SkyBlockPlugin.getPlugin(), this::load);
    }

    @Override
    public void load() {
        playerData = new SkyBlockPlayerData(this);
    }

    @Override
    public void register() {

    }

    @Override
    public void unregister() {

    }

    public static SkyBlockPlayer getOrCreate(Player player) {
        UUID id = player.getUniqueId();
        ISkyBlockPlayer skyblockPlayer = PlayersManager.get(id);
        return skyblockPlayer != null ? (SkyBlockPlayer) skyblockPlayer : new SkyBlockPlayer(id);
    }

}
