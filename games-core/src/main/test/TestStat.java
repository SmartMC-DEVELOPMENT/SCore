import us.smartmc.gamescore.instance.player.statistic.PlayerStatistic;
import us.smartmc.gamescore.instance.player.statistic.PlayerStatisticName;

@PlayerStatisticName(name = "test_number")
public class TestStat extends PlayerStatistic<Integer> {

    @Override
    public Integer getDefaultValue() {
        return 0;
    }
}
