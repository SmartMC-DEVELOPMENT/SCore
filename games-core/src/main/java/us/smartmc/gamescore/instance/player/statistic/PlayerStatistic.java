package us.smartmc.gamescore.instance.player.statistic;

import us.smartmc.gamescore.instance.player.GameCorePlayer;

public abstract class PlayerStatistic<O> implements IPlayerStatistic<O> {

    protected final String name;

    public PlayerStatistic() {
        this.name = getName(this);
    }

    @Override
    public void updateValue(GameCorePlayer gamePlayer, O value) {
        gamePlayer.getStats().put(name, value);
    }

    @Override
    public O getValue(GameCorePlayer gamePlayer) {
        return (O) gamePlayer.getStats().get(name);
    }

    private static String getName(PlayerStatistic<?> instance) {
        PlayerStatisticName annotation = instance.getClass().getDeclaredAnnotation(PlayerStatisticName.class);
        if (annotation != null) {
            return annotation.name();
        }
        return instance.getStatisticName();
    }

    @Override
    public String getStatisticName() {
        return name;
    }
}
