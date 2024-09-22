package us.smartmc.gamescore.instance.timer;

public interface ICountdownTimer extends ITimer {

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
