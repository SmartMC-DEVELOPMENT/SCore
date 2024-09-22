package us.smartmc.gamescore.instance.player.statistic;

import us.smartmc.gamescore.instance.player.GameCorePlayer;

public interface IPlayerStatistic<O> {

    String getStatisticName();

    O getValue(GameCorePlayer gamePlayer);
    void updateValue(GameCorePlayer gamePlayer, O value);
    O getDefaultValue();
}
