package us.smartmc.gamescore.instance.game.timer;

public interface IGameCountdownTimer extends IGameTimer {

    long getDuration();
    long getSecondsLeft();

    void start();

    default void perform() {
        if (getSecondsLeft() <= 0) {
            stop();
            performEnd();
        }
    }

    void performEnd();

    void performStart();

}
