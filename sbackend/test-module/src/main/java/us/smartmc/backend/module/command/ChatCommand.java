package us.smartmc.backend.module.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.module.PlayersServiceServer;

public class ChatCommand extends BackendCommandExecutor {

    public ChatCommand() {
        super("chat");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        /*StringBuilder builder = new StringBuilder();
        for (int index = 1; index < args.length; index++) {
            builder.append(args[index]).append(" ");
        }
        if (builder.toString().endsWith(" "))
            builder.deleteCharAt(builder.length() -1);

        ServicesManager.performWhenLoaded(PlayersServiceServer.class, playersServiceServer -> {
            playersServiceServer.sendMessage("imsergioh", builder.toString());
        });*/
    }
}
