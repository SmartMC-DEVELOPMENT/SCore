package us.smartmc.core.instance;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

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
            SimplePluginManager manager = ((SimplePluginManager) Bukkit.getPluginManager());
            Field field = manager.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            SimpleCommandMap commandMap = (SimpleCommandMap) field.get(manager);

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
