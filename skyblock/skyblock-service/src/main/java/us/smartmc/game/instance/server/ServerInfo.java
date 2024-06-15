package us.smartmc.game.instance.server;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.skyblock.instance.SkyBlockServerType;

@Getter
public class ServerInfo implements IServerInfo {

    private final String id;

    private final SkyBlockServerType modeType;

    @Setter
    private int onlineCount;

    public ServerInfo(String id, SkyBlockServerType modeType) {
        this.id = id;
        this.modeType = modeType;
    }

}
