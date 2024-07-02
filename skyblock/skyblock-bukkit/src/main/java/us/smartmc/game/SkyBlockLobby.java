package us.smartmc.game;

import us.smartmc.game.listener.MainIslandsListeners;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockLobby implements ISkyBlockAPI {

    @Override
    public void onEnable() {
        SkyBlockPlugin.registerListeners(new MainIslandsListeners());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.SPAWN;
    }
}
