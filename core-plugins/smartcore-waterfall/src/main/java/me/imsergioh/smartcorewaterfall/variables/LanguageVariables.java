package me.imsergioh.smartcorewaterfall.variables;

import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.instance.VariableListener;
import us.smartmc.core.pluginsapi.language.Language;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LanguageVariables extends VariableListener<ProxiedPlayer> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(ProxiedPlayer player, String message) {
        if (player == null || message == null || !message.contains("<lang.")) {
            return message;
        }

        Language language = PlayerLanguages.getLanguage(player.getUniqueId());
        String[] args = message.split(" ");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!arg.contains("<lang.")) {
                continue;
            }
            String messageHolder = arg.split("\\.")[1];
            String path = arg.split("\\.")[2].replace(">", "");
            Object object = LanguagesHandler
                    .get(language)
                    .get(messageHolder)
                    .get(path);

            if (object instanceof String) {
                args[i] = (String) object;
            } else {
                args[i] = "LANGUAGE_ERROR";
            }
        }
        return parse(player, String.join(" ", args));
    }
}
