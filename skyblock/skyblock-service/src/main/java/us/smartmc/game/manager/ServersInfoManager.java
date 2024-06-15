package us.smartmc.game.manager;

import us.smartmc.game.instance.server.IServerInfo;

import java.util.HashMap;
import java.util.Map;

public class ServersInfoManager {

    private static final Map<String, IServerInfo> serverInfos = new HashMap<>();

    public static void register(IServerInfo serverInfo) {
        if (serverInfo == null) return;
        serverInfos.put(serverInfo.getId(), serverInfo);
    }

}
