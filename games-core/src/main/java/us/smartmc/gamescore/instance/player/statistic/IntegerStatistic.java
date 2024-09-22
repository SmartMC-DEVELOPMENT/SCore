package us.smartmc.gamescore.instance.player.statistic;

import lombok.Getter;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public abstract class IntegerStatistic extends PlayerStatistic<Integer> {

    @Getter
    protected final int max = Integer.MAX_VALUE;
    @Getter
    protected final int min = Integer.MIN_VALUE;

    public void increase(GameCorePlayer gameCorePlayer) {
        Integer newAmount = getValue(gameCorePlayer) + 1;
        // Check max and set if exceeded
        int max = getMax();
        if (newAmount >= max) {
            newAmount = max;
        }

        updateValue(gameCorePlayer, newAmount);
    }

    public void decrease(GameCorePlayer gameCorePlayer) {
        Integer newAmount = getValue(gameCorePlayer) - 1;
        // Check min and set if exceeded
        int min = getMin();
        if (newAmount <= min) {
            newAmount = min;
        }

        updateValue(gameCorePlayer, newAmount);
    }
}
