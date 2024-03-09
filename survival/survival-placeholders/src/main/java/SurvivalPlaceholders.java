import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurvivalPlaceholders extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (params == null) return null;
        if (!params.contains("rank")) return params;
        String prefix = getPrefix(player, true);
        // <RANK>
        if (prefix != null && prefix.length() >= 3) {
            return prefix;
        } else {
            return "§c¡Adquiérelo!";
        }
    }

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
            return "&7";
        }
    }

    @Override
    public String getIdentifier() {
        return "survival";
    }

    @Override
    public String getAuthor() {
        return "ImSergioh";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
