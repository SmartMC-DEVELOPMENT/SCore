package us.smartmc.backend.connection.test;

public class TimingTestInstance {

    private long start, end;


    public void registerStart() {
        start = System.currentTimeMillis();
    }

    public void registerEnd() {
        end = System.currentTimeMillis();
    }

    public long getDifference() {
        long min = Math.min(start, end);
        long max = Math.max(start, end);
        return max - min;
    }

}
