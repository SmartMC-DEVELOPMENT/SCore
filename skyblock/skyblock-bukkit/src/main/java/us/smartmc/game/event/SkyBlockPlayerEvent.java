package us.smartmc.game.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.game.instance.SkyBlockPlayer;

public class SkyBlockPlayerEvent extends SkyBlockEvent {

    @Getter
    private final SkyBlockPlayer skyBlockPlayer;

    public SkyBlockPlayerEvent(SkyBlockPlayer skyBlockPlayer) {
        this.skyBlockPlayer = skyBlockPlayer;
    }

    public Player getBukkitPlayer() {
        return skyBlockPlayer.getBukkitPlayer();
    }

}
