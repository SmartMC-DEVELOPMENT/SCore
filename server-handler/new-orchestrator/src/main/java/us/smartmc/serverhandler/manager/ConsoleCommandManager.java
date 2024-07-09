package us.smartmc.serverhandler.manager;

import us.smartmc.serverhandler.ConsoleColors;
import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.util.CommandUtilities;

import java.util.HashMap;
import java.util.Map;

public class ConsoleCommandManager {

    private static final HashMap<String, ConsoleCommand> commands = new HashMap<>();

    public static void register(ConsoleCommand... cmds) {
        for (ConsoleCommand command : cmds)  {
            commands.put(command.getName(), command);
        }
    }

    protected static String findCommandByNameOrAlias(String input) {
        for (Map.Entry<String, ConsoleCommand> entry : commands.entrySet()) {
            final String name = entry.getKey();
            if (name.equalsIgnoreCase(input)) {
                return name;
            }

            final String[] aliases = entry.getValue().getAliases();
            if (aliases == null) {
                continue;
            }
            for (String alias : aliases) {
                if (!alias.equalsIgnoreCase(input)) {
                    continue;
                }
                return name;
            }
        }
        return null;
    }

    public static void perform(String cmdLine) {
        final String[] splitMessage = cmdLine.split(" ");
        String command = splitMessage[0];
        if (command == null) {
            return;
        }
        command = command.toLowerCase();

        final String commandName = findCommandByNameOrAlias(command);
        if (commandName == null) {
            CommandUtilities.sendError("No command found for '%s'!", command);
            return;
        }
        final ConsoleCommand registeredCommand = commands.get(commandName);
        if (registeredCommand == null) {
            return;
        }

        for (int i = 1; i < splitMessage.length; i++) {
            final String data = splitMessage[i];
            if (data == null || data.isEmpty()) {
                CommandUtilities.sendError("Invalid argument in position '%s'!", i);
                return;
            }
            splitMessage[i] = data.trim();
        }

        final int argSize = splitMessage.length - 1;

        final String[] args = new String[argSize];
        System.arraycopy(splitMessage, 1, args, 0, argSize);

        final int minArgs = registeredCommand.getMinArgs();
        final int maxArgs = registeredCommand.getMaxArgs();

        final String usage = registeredCommand.getUsage();
        final String correctUsage = String.format("\nThe correct usage for this command is: %s", usage);

        if (minArgs >= 0 && argSize < minArgs) {
            CommandUtilities.sendError("Not enough arguments! Minimum required: %s%s", minArgs, correctUsage);
            return;
        }
        if (maxArgs >= 0 && argSize > maxArgs) {
            CommandUtilities.sendError("Too many arguments! Maximum allowed: %s%s", maxArgs, correctUsage);
            return;
        }

        registeredCommand.execute(cmdLine, args);
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
