package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.VariableListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.util.VariableUtil;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuckPermsVariables extends VariableListener<Player> {

    public static String getPrefix(Player player, boolean space) {
        return "";
    }

    public static String getFirstColor(String input) {
        Pattern pattern = Pattern.compile("(&[0-9a-fk-or])");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "&7";
        }
    }

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        if (message == null) return null;
        if (!message.contains("<rank>") && !message.contains("<chat.prefix")) return message;
        String prefix = getPrefix(player, true);
        // <RANK>
        if (prefix != null && prefix.length() >= 3) {
            message = VariableUtil.replace(message, "<rank>", s -> prefix);
        } else {
            String acquireRankMessage = LanguagesHandler.get(PlayerLanguages.get(player.getUniqueId())).get("general").getString("acquireRank");
            message = VariableUtil.replace(message, "<rank>", s-> "<red>" + acquireRankMessage);
        }

        // <CHAT.PREFIX>
        message = VariableUtil.replace(message, "<chat.prefix>", s -> getPrefix(player, true));
        if (message.contains("<chat.prefix.")) {
            if (message.contains("<chat.prefix.color>")) {
                return getFirstColor(prefix);
            }
            for (String arg : message.split(" ")) {
                if (!arg.contains("<chat.prefix.")) continue;
                boolean space = Boolean.parseBoolean(arg.split("\\.")[2].replace(">", "").toLowerCase());
                message = VariableUtil.replace(message, arg, s -> getPrefix(player, space));
            }
        }
        return message;
    }

}
