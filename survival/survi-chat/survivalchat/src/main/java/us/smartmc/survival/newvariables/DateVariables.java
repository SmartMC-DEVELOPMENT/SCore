package us.smartmc.survival.newvariables;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateVariables extends VariableListener<Player> {


    @Override
    public String parse(String s) {
       return s;
    }

    @Override
    public String parse(Player player, String s) {
        if (!s.contains("<date>")) return s;
        String pattern = "dd/MM/yy";
        Language language = PlayerLanguages.get(player.getUniqueId());
        if (language.equals(Language.EN)) pattern = "MM/dd/yy";
        return s.replace("<date>", getDateFormat(pattern));
    }

    private String getDateFormat(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
