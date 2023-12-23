package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateVariables extends VariableListener<Player> {


    @Override
    public String parse(String s) {
        if (!s.contains("<date>")) return s;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDateTime now = LocalDateTime.now();
        s = s.replace("<date>", dtf.format(now));
        return s;
    }

    @Override
    public String parse(Player player, String s) {
        return parse(s);
    }
}
