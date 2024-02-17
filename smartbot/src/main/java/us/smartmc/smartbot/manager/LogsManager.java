package us.smartmc.smartbot.manager;

import us.smartmc.smartbot.instance.log.LogChannelRegistry;

import java.util.HashMap;
import java.util.Map;

public class LogsManager {

    private final Map<String, LogChannelRegistry> logsChannels = new HashMap<>();

    public void register(LogChannelRegistry... registries) {
        for (LogChannelRegistry registry : registries) {
            logsChannels.put(registry.getId(), registry);
        }
    }
}
