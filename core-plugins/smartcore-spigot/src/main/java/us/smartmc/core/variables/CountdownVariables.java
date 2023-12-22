package us.smartmc.core.variables;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.instance.ClickableComponent;
import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.instance.VariableListener;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.util.TimeFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountdownVariables extends VariableListener<Player> {

    // FORMAT: <COUNTDOWN:1699634902>


    public String parse(Language language, String s) {
        if (!s.contains("<countdown:")) return s;
        Pattern pattern = Pattern.compile("<countdown:(\\d+)>");
        Matcher matcher = pattern.matcher(s.toLowerCase());
        long timestamp;
        if (matcher.find()) {
            String countdown = matcher.group(0);
            timestamp = Long.parseLong(matcher.group(1));
            s = s.replace(countdown, TimeFormatter.formatDurationUntil(language, timestamp));
        }
        return s;
    }

    @Override
    public String parse(String s) {
        if (!s.toLowerCase().contains("<countdown:")) return s;
        return parse(Language.getDefault(), s);
    }

    @Override
    public String parse(Player player, String s) {
        if (!s.toLowerCase().contains("<countdown:")) return s;
        return parse(PlayerLanguages.get(player.getUniqueId()), s);
    }
}
