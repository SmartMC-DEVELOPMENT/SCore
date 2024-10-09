import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Finder {

    public static void main(String[] args) {
        long timestamp = 1727992800L;
        long nanos = TimeUnit.MILLISECONDS.toNanos(timestamp);
        System.out.println("Nanos: " + nanos);

        boolean found = false;
        while (!found) {
            Random random = new Random(nanos);

            int[] numbers = new int[5];

            int index = 0;
            while (index < numbers.length) {
                int ran = random.nextInt(50) + 1;
                numbers[index] = ran;
                index++;
            }
            int result = Arrays.compare(numbers, new int[]{4, 8, 16, 17, 20});
            if (result == 0) {
                System.out.println(Arrays.toString(numbers));
                System.out.println("YES! FOUND IT AT " + nanos + " (" + TimeUnit.NANOSECONDS.toMillis(nanos) + ")");
            }
            nanos++;
        }
    }

}
