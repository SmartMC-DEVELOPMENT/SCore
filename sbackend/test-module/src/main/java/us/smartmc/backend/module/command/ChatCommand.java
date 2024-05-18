package us.smartmc.backend.module.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.Arrays;

public class ChatCommand extends BackendCommandExecutor {

    public ChatCommand() {
        super("chat");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        System.out.println("Received chat! " + Arrays.asList(args));
    }
}
