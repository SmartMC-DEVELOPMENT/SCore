package us.smartmc.skyblock;

import us.smartmc.skyblock.instance.SkyBlockServerType;

public interface ISkyBlockAPI {

    default void onEnable() {}
    default void onDisable() {}

    SkyBlockServerType getBlockModeType();

}
