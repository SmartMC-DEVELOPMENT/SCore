package us.smartmc.npcsmodule.variables;


import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NPCEmbedsVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        if (!message.contains("<embed.online")) return message;
        return parse(Language.getDefault(), message);
    }

    @Override
    public String parse(Player player, String message) {
        if (!message.contains("<embed.online")) return message;
        return parse(Language.getDefault(), message);
    }

    public String parse(Language language, String message) {
        Pattern pattern = Pattern.compile("<embed.online.(.*?)>");
        Matcher matcher = pattern.matcher(message);
        StringBuffer output = new StringBuffer();

        if (matcher.find()) {
            String serverId = matcher.group(1);
            int online = Bukkit.getOnlinePlayers().size();

            String path = "onlinePlayers";

            if (online == 1) {
                path = "onlinePlayer";
            }

            if (language == null) language = Language.getDefault();

            String prefixTextPlayers = LanguagesHandler.get(language)
                    .get("npcs-module").getString(path);
            String finalMessage = online + " " + prefixTextPlayers;

            matcher.appendReplacement(output, finalMessage);
        }
        matcher.appendTail(output);
        return output.toString();
    }

}
