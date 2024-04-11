package us.smartmc.backend.connection.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.Arrays;

public class TestCommand extends BackendCommandExecutor {

    public TestCommand() {
        super("test");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        System.out.println("TEST! " + Arrays.asList(args));
    }
}
