package us.smartmc.backend.module.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.module.PlayersServiceServer;

import java.util.Arrays;

public class ChatCommand extends BackendCommandExecutor {

    public ChatCommand() {
        super("chat");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        System.out.println("Received chat! " + Arrays.asList(args));

        StringBuilder builder = new StringBuilder(args[1]);

        for (int index = 2; index < args.length; index++) {
            builder.append(args[index]).append(" ");
        }
        if (builder.toString().endsWith(" "))
            builder.deleteCharAt(builder.length() -1);

        ServicesManager.performWhenLoaded(PlayersServiceServer.class, playersServiceServer -> {
            playersServiceServer.sendMessage("imsergioh", builder.toString());
        });
    }
}
