package us.smartmc.game;

import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockIslands implements ISkyBlockAPI {


    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.ISLAND;
    }
}
