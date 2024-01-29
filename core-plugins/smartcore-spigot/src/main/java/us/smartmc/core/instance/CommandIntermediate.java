package us.smartmc.core.instance;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.lang.reflect.Field;


public class CommandIntermediate {

    private final String name;

    public CommandIntermediate(String name) {
        this.name = name;
    }

    public boolean isAtCommandMap() {
        String withoutSlash = name.replaceFirst("/", "");
        if (check(withoutSlash)) return true;
        return check(name);
    }

    private boolean check(String cmdName) {
        if (cmdName == null) return false;
        try {
            SimpleCommandMap commandMap;
            Field field = CraftServer.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (SimpleCommandMap) field.get(Bukkit.getServer());

            for (Command command : commandMap.getCommands()) {
                if (cmdName.equalsIgnoreCase(command.getName())) return true;
                for (String alias : command.getAliases()) {
                    if (alias.equalsIgnoreCase(cmdName)) return true;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
