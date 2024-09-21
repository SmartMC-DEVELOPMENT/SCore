package instance;

import us.smartmc.gamescore.instance.MapManager;

import java.util.UUID;

public class TestManager extends MapManager<UUID, String> {

    @Override
    public String createValueByKey(UUID key) {
        return "TEST-" + key.toString();
    }
}
