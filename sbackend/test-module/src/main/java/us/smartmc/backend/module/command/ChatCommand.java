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

        ServicesManager.performWhenLoaded(PlayersServiceServer.class, playersServiceServer -> {
            playersServiceServer.sendMessage("imsergioh", "Mensaje recibido " + Arrays.asList(args));
        });
    }
}
