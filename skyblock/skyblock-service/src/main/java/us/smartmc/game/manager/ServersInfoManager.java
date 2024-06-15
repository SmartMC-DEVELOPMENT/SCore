package us.smartmc.game.manager;

import us.smartmc.game.instance.server.IServerInfo;

import java.util.HashMap;
import java.util.Map;

public class ServersInfoManager {

    private static final Map<String, IServerInfo> serverInfos = new HashMap<>();

    public static <T extends  IServerInfo> T unregister(String id, Class<T> type) {
        IServerInfo info = serverInfos.remove(id);
        if (info.getClass().isAssignableFrom(type)) return type.cast(info);
        return null;
    }

    public static void register(IServerInfo serverInfo) {
        if (serverInfo == null) return;
        serverInfos.put(serverInfo.getId(), serverInfo);
    }

    public static <T extends IServerInfo> T getServerInfo(String id, Class<? extends T> type) {
        IServerInfo info = serverInfos.get(id);
        if (info.getClass().isAssignableFrom(type)) return type.cast(info);
        return null;
    }

}
