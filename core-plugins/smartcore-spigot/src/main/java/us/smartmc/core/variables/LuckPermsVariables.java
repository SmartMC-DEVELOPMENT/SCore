package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.util.VariableUtil;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuckPermsVariables extends VariableListener<Player> {

    public static String getPrefix(Player player, boolean space) {
        net.luckperms.api.LuckPerms lp = net.luckperms.api.LuckPermsProvider.get();
        try {
            String prefix = Objects.requireNonNull(lp.getUserManager().getUser(player.getUniqueId())).getCachedData().getMetaData().getPrefix();
            String prefixColor = getFirstColor(prefix);
            if (space && prefix != null && prefix.length() > 3) prefix += " " + prefixColor;

            if (prefix != null && prefix.length() > 3) {
                return prefix;
            } else {
                return prefixColor;
            }
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String getFirstColor(String input) {
        Pattern pattern = Pattern.compile("(&[0-9a-fk-or])");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "&r";
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
            message = VariableUtil.replace(message, "<rank>", prefix);
        } else {
            String acquireMessage = "§c" + SmartCorePlayer.get(player)
                    .getLanguageMessage("general", "acquireRank");
            message = VariableUtil.replace(message, "<rank>", acquireMessage);
        }

        // <CHAT.PREFIX>
        message = VariableUtil.replace(message, "<chat.prefix>", getPrefix(player, true));
        if (message.contains("<chat.prefix.")) {
            for (String arg : message.split(" ")) {
                if (!arg.contains("<chat.prefix.")) continue;
                boolean space = Boolean.parseBoolean(arg.split("\\.")[2].replace(">", "").toLowerCase());
                message = VariableUtil.replace(message, arg, getPrefix(player, space));
            }
        }
        return message;
    }

}
