package us.smartmc.game.event;

import lombok.Getter;
import us.smartmc.game.instance.SkyBlockPlayer;
import us.smartmc.game.instance.SkyBlockPlayerData;
import us.smartmc.game.instance.SkyBlockPlayerIslandData;

@Getter
public class SkyBlockIslandPlayerDataLoadedEvent extends SkyBlockPlayerEvent {

    private final SkyBlockPlayerIslandData data;

    public SkyBlockIslandPlayerDataLoadedEvent(SkyBlockPlayer player, SkyBlockPlayerIslandData data) {
        super(player);
        this.data = data;
    }

}
