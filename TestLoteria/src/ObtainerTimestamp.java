import java.util.concurrent.TimeUnit;

public class ObtainerTimestamp {

    public static void main(String[] args) {
        long nanos = 1728068702515842L;
        long seed = TimeUnit.NANOSECONDS.toMillis(nanos);
        System.out.println(seed);
    }

}
