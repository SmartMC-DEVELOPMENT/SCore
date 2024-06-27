package us.smartmc.game.event;

import lombok.Getter;
import us.smartmc.game.instance.SkyBlockPlayer;
import us.smartmc.game.instance.SkyBlockPlayerData;

@Getter
public class SkyBlockPlayerDataLoadedEvent extends SkyBlockPlayerEvent {

    private final SkyBlockPlayerData data;

    public SkyBlockPlayerDataLoadedEvent(SkyBlockPlayer player, SkyBlockPlayerData data) {
        super(player);
        this.data = data;
    }

}
