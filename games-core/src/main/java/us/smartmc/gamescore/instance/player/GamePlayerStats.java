package us.smartmc.gamescore.instance.player;

import lombok.Getter;
import org.bson.Document;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.manager.SetManager;
import us.smartmc.gamescore.instance.player.statistic.IPlayerStatistic;
import us.smartmc.gamescore.manager.PlayerStatisticsManager;

import java.util.UUID;

@Getter
public class GamePlayerStats extends MapManager<String, Object> {

    private final UUID uuid;

    public GamePlayerStats(UUID uuid) {
        this.uuid = uuid;
        registerDefaultStats();
    }

    private void registerDefaultStats() {
        PlayerStatisticsManager manager = SetManager.getManager(PlayerStatisticsManager.class);
        if (manager == null) return;
        for (IPlayerStatistic<?> statistic : manager.stream().toList()) {
            register(statistic.getStatisticName());
        }
    }

    public Document getDataDocument() {
        Document document = new Document("_id", uuid.toString());
        document.putAll(this);
        return document;
    }

    @Override
    public Object createValueByKey(String key) {
        Object defaultValue = null;

        // Find by key default value from stats registered
        for (IPlayerStatistic<?> statistic : getManager().stream().toList()) {
            if (statistic.getStatisticName().equals(key)) {
                defaultValue = statistic.getDefaultValue();
                break;
            }
        }

        // Return null if not variable detected or defaultValue from Stats Instance
        return defaultValue;
    }

    private static PlayerStatisticsManager getManager() {
        return PlayerStatisticsManager.getManager(PlayerStatisticsManager.class);
    }
}
