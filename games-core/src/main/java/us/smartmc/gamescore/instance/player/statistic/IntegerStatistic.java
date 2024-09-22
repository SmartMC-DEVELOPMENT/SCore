package us.smartmc.gamescore.instance.player.statistic;

import lombok.Getter;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public abstract class IntegerStatistic extends PlayerStatistic<Integer> {

    @Getter
    protected final int min = Integer.MIN_VALUE;

    public void increase(GameCorePlayer gameCorePlayer) {
        Integer newAmount = getValue(gameCorePlayer) + 1;
        updateValue(gameCorePlayer, newAmount);
    }

    public void decrease(GameCorePlayer gameCorePlayer) {
        Integer newAmount = getValue(gameCorePlayer) - 1;
        int min = getMin();
        if (newAmount <= min) {
            newAmount = min;
        }
        updateValue(gameCorePlayer, newAmount);
    }
}
