package us.smartmc.gamescore.instance.player.statistic;

import us.smartmc.gamescore.instance.player.GameCorePlayer;

public abstract class IntegerStatistic extends PlayerStatistic<Integer> {

    public void increase(GameCorePlayer gameCorePlayer) {
        Integer newAmount = getValue(gameCorePlayer) + 1;
        updateValue(gameCorePlayer, newAmount);
    }
}
