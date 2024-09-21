package instance.manager;

import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class TestMapManager extends MapManager<UUID, String> {

    @Override
    public String createValueByKey(UUID key) {
        return "TEST-" + key.toString();
    }
}
