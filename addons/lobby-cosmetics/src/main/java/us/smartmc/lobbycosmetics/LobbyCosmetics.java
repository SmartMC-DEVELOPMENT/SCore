package us.smartmc.lobbycosmetics;

import lombok.Getter;
import us.smartmc.smartaddons.plugin.AddonPlugin;

public class LobbyCosmetics extends AddonPlugin {

    @Getter
    private static LobbyCosmetics instance;

    @Override
    public void start() {
        instance = this;
    }

    @Override
    public void stop() {

    }
}
