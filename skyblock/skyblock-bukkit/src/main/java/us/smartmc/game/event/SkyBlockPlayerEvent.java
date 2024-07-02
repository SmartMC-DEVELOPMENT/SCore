package us.smartmc.game.event;

import org.bukkit.entity.Player;
import us.smartmc.game.instance.SkyBlockPlayer;

public class SkyBlockPlayerEvent extends SkyBlockEvent {

    private final SkyBlockPlayer skyBlockPlayer;

    public SkyBlockPlayerEvent(SkyBlockPlayer skyBlockPlayer) {
        this.skyBlockPlayer = skyBlockPlayer;
    }

    public Player getBukkitPlayer() {
        return skyBlockPlayer.getBukkitPlayer();
    }

    public SkyBlockPlayer getSkyBlockPlayer() {
        return skyBlockPlayer;
    }
}
