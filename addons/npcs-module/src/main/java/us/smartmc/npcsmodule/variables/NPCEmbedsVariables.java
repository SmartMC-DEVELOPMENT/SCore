package us.smartmc.npcsmodule.variables;


import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.instance.VariableListener;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.variables.CountVariables;

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
        SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
        if (corePlayer != null) return parse(SmartCorePlayer.get(player).getLanguage(), message);
        return parse(Language.getDefault(), message);
    }

    public String parse(Language language, String message) {
        Pattern pattern = Pattern.compile("<embed.online.(.*?)>");
        Matcher matcher = pattern.matcher(message);
        StringBuffer output = new StringBuffer();

        if (matcher.find()) {
            String serverId = matcher.group(1);
            long online = Long.parseLong(CountVariables.getCountOf(serverId));
            String path = "online_players";

            if (online == 1) {
                path = "online_player";
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
