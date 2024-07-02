package us.smartmc.game.event;

import us.smartmc.game.instance.SkyBlockPlayer;
import us.smartmc.game.instance.SkyBlockPlayerData;

public class SkyBlockPlayerDataLoadedEvent extends SkyBlockEvent {

    private final SkyBlockPlayer skyBlockPlayer;
    private final SkyBlockPlayerData data;

    public SkyBlockPlayerDataLoadedEvent(SkyBlockPlayer player, SkyBlockPlayerData data) {
        this.skyBlockPlayer = player;
        this.data = data;
    }

    public SkyBlockPlayer getSkyBlockPlayer() {
        return skyBlockPlayer;
    }

    public SkyBlockPlayerData getData() {
        return data;
    }
}
