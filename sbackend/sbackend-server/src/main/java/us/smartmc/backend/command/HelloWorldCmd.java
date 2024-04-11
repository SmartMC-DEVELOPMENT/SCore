package us.smartmc.backend.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class HelloWorldCmd extends BackendCommandExecutor {

    public HelloWorldCmd() {
        super("helloWorld");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        long difference = System.currentTimeMillis() - Long.parseLong(args[0]);
        System.out.println("DIFERENCIA: " + difference);
    }
}
