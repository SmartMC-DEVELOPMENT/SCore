package us.smartmc.game.instance.server;

import us.smartmc.skyblock.instance.SkyBlockServerType;

public interface IServerInfo {

    int getOnlineCount();

    SkyBlockServerType getModeType();
    String getId();

}
