package us.smartmc.gamescore.adminplayer;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.PlayerRegionSelectionsManager;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.UUID;

@Getter
public class PlayerRegionSelectSession {

    private final UUID uuid;

    @Setter
    private Location pos1, pos2;

    public PlayerRegionSelectSession(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        BukkitUtil.getPlayer(uuid).ifPresent(player -> {
            player.sendMessage(ChatUtil.parse("&aPosición 1 establecida! (" + pos1.toString() + ")"));
        });
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        BukkitUtil.getPlayer(uuid).ifPresent(player -> {
            player.sendMessage(ChatUtil.parse("&aPosición 2 establecida! (" + pos2.toString() + ")"));
        });
    }

    public Cuboid buildCuboid() {
        return new Cuboid(pos1, pos2);
    }

    public static PlayerRegionSelectSession get(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerRegionSelectionsManager manager = MapManager.getManager(PlayerRegionSelectionsManager.class);
        if (manager == null) return null;
        return manager.getOrCreate(uuid);
    }

}
