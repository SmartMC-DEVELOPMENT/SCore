package us.smartmc.serverhandler.manager;

import us.smartmc.serverhandler.ConsoleColors;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;

import java.util.HashMap;

public class BackendCommandManager {

    private static final HashMap<String, BackendCommand> commands = new HashMap<>();

    public static void register(BackendCommand... cmds) {
        for (BackendCommand command : cmds)  {
            commands.put(command.getName(), command);
        }
    }

    public static void perform(ConnectionHandler handler, BackendCommandExecuteRequest request) {
        String name = request.getName();
        if (!commands.containsKey(name)) {
            System.out.println(ConsoleColors.RED + "No backend command found for '" + name + "'!");
            return;
        }
        String[] args = request.getArgs();
        commands.get(name).execute(handler, request.getLabel(), args);
    }
}
