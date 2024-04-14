package us.smartmc.smartcore.smartcorevelocity.variables;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;

import java.util.List;

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
            String path = arg.replaceAll("^.*?\\.([^>]+)>$", "$1");
            if (path.startsWith(messageHolder + ".")) path = path.replace(messageHolder + ".", "");
            Object object = LanguagesHandler
                    .get(language)
                    .get(messageHolder)
                    .get(path);

            if (object instanceof String) {
                args[i] = (String) object;
            } else if (object instanceof List) {
                StringBuilder stringBuilder = new StringBuilder();
                List<?> list = (List<?>) object;
                for (Object o : list) {
                    stringBuilder.append(o.toString() + "\n");
                }
                args[i] = stringBuilder.toString();
            } else {
                args[i] = object.toString();
            }
        }
        return parse(player, String.join(" ", args));
    }
}
