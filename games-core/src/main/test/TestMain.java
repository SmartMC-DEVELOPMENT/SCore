import instance.manager.TestSetManager;

import java.util.UUID;

public class TestMain {

    public static void main(String[] args) {
        TestSetManager testManager = new TestSetManager();
        testManager.add(UUID.randomUUID());

        System.out.println("VALUE?" + testManager);
    }

}
