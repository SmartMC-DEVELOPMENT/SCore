package us.smartmc.game;

import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockLobby implements ISkyBlockAPI {


    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.SPAWN;
    }
}
