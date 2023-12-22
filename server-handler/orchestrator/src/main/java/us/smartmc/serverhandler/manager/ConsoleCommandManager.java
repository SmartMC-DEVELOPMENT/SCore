package us.smartmc.serverhandler.manager;

import us.smartmc.serverhandler.ConsoleColors;
import us.smartmc.serverhandler.OrchestratorMain;
import us.smartmc.serverhandler.executor.ConsoleCommand;

import java.util.HashMap;

public class ConsoleCommandManager {

    private static final HashMap<String, ConsoleCommand> commands = new HashMap<>();

    public static void register(ConsoleCommand... cmds) {
        for (ConsoleCommand command : cmds)  {
            commands.put(command.getName(), command);
        }
    }

    public static void perform(String cmdLine) {
        String name = cmdLine.split(" ")[0].toLowerCase();
        if (!commands.containsKey(name)) {
            OrchestratorMain.log(ConsoleColors.RED + "No command found for '" + name + "'!");
            return;
        }
        String[] args = cmdLine.replaceFirst(name + " ", "").split(" ");
        commands.get(name).execute(cmdLine, args);
    }

    public static StringBuilder getCommandInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Commands:\n");

        for (String name : commands.keySet()) {
            builder.append(ConsoleColors.WHITE +  "  - " + ConsoleColors.GREEN + name + ConsoleColors.WHITE +
                    " | " + ConsoleColors.BLUE + commands.get(name).getDescription() + ConsoleColors.RESET + "\n");
        }
        return builder;
    }

}
