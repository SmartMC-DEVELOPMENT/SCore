import us.smartmc.gamescore.instance.player.statistic.IntegerStatistic;
import us.smartmc.gamescore.instance.player.statistic.PlayerStatistic;
import us.smartmc.gamescore.instance.player.statistic.PlayerStatisticName;

@PlayerStatisticName(name = "test_number")
public class TestStat extends IntegerStatistic {

    @Override
    public Integer getDefaultValue() {
        return 77;
    }

    @Override
    public int getMax() {
        return 30;
    }

    @Override
    public int getMin() {
        return 0;
    }
}
