package us.smartmc.lobbymodule.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.util.VariableUtil;
import us.smartmc.core.variables.CountVariables;
import us.smartmc.lobbymodule.handler.MaxSlotsInfoManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        if (message.contains("<serverCapacity.")) {
            message = parseCapacityVariables(message);
        }
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        return parse(message);
    }

    private String parseCapacityVariables(String message) {
        Pattern pattern = Pattern.compile("<serverCapacity\\.([^>]+)>");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String serverId = matcher.group(1);

            String count = CountVariables.getCountOf(serverId);
            int maxCapacity = MaxSlotsInfoManager.getMaxSlotsOf(serverId);

            double percentage = Double.parseDouble(count) / maxCapacity * 100;

            String color;
            if (percentage > 1 && percentage < 50){
                color = "&a";
            }else if(percentage > 50 && percentage < 90){
                color = "&6";
            }else{
                color = "&c";
            }

            StringBuilder builder = new StringBuilder();
            int casillasAPintar = (int) Math.ceil((percentage / 100) * 10);
            builder.append((color + "■").repeat(Math.max(0, casillasAPintar)));

            int restantes = 10 - casillasAPintar;

            builder.append("&7■".repeat(Math.max(0, restantes)));

            String result = matcher.replaceFirst(builder.toString());
            return result.replaceAll("[<>]", "");
        }

        return message;
    }

}
