package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MiniMessageAdapterVariables extends VariableListener<Player> {

    private static final Map<String, String> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put("<black>", "&0");
        COLOR_MAP.put("<dark_blue>", "&1");
        COLOR_MAP.put("<dark_green>", "&2");
        COLOR_MAP.put("<dark_aqua>", "&3");
        COLOR_MAP.put("<dark_red>", "&4");
        COLOR_MAP.put("<dark_purple>", "&5");
        COLOR_MAP.put("<gold>", "&6");
        COLOR_MAP.put("<gray>", "&7");
        COLOR_MAP.put("<dark_gray>", "&8");
        COLOR_MAP.put("<blue>", "&9");
        COLOR_MAP.put("<green>", "&a");
        COLOR_MAP.put("<aqua>", "&b");
        COLOR_MAP.put("<red>", "&c");
        COLOR_MAP.put("<light_purple>", "&d");
        COLOR_MAP.put("<yellow>", "&e");
        COLOR_MAP.put("<white>", "&f");
    }

    @Override
    public String parse(String s) {
        for (Map.Entry<String, String> entry : COLOR_MAP.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }
        return s;
    }

    @Override
    public String parse(Player player, String s) {
        return parse(s);
    }
}
