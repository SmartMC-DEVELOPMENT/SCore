package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.smartmc.serverhandler.manager.CountsManager;


public class CountVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        if (!message.contains("<count.")) return message;
        for (String arg : message.split(" ")) {
            if (arg.contains("<count.")) {
                arg = ChatColor.stripColor(arg.replace("&", "§"));
                String idName = arg.replace("<count.", "").replace(">", "");
                if (idName.equals("all")) {
                    message = message.replaceFirst(arg, CountsManager.getAllCount(CountsManager.values()));
                } else {
                    message = message.replaceFirst(arg, CountsManager.getCountOf(idName));
                }
            }
        }
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        return parse(message);
    }



}
