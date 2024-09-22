package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.instance.manager.SetManager;
import us.smartmc.gamescore.instance.player.statistic.IPlayerStatistic;

import java.util.List;

public class PlayerStatisticsManager extends SetManager<IPlayerStatistic<?>> {

    public void register(IPlayerStatistic<?>... statistics) {
        addAll(List.of(statistics));
    }
}
