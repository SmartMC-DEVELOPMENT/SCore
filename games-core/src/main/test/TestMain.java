import instance.TestManager;

import java.util.UUID;

public class TestMain {

    public static void main(String[] args) {
        TestManager testManager = new TestManager();
        UUID randomUUID = UUID.randomUUID();
        String value = testManager.register(randomUUID);

        System.out.println("VALUE?" + value);
    }

}
