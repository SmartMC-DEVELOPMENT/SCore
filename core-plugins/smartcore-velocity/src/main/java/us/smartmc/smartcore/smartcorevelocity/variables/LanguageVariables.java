package us.smartmc.smartcore.smartcorevelocity.variables;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;

public class LanguageVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
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
