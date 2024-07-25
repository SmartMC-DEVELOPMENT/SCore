package us.smartmc.addon.holograms.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;

public class TickVariable extends VariableListener<Player> {

    private static long ticks = 0;

    @Override
    public String parse(String s) {
        if (!s.contains("<ticks>")) return s;
        ticks++;
        s = s.replace("<ticks>", String.valueOf(ticks));

        return  s;
    }

    @Override
    public String parse(Player player, String s) {
        return parse(s);
    }
}
