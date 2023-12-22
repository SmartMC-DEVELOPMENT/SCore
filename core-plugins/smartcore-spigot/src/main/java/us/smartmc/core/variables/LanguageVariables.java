package us.smartmc.core.variables;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.instance.VariableListener;
import us.smartmc.core.pluginsapi.language.Language;

public class LanguageVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        if (!message.contains("<lang.")) return message;
        return get(null, message);
    }

    @Override
    public String parse(Player player, String message) {
        if (player == null) return message;
        if (!message.contains("<lang.")) return message;
        return get(player, message);
    }

    private String get(Player player, String message) {
        Language language = Language.getDefault();
        if (player != null) language = PlayerLanguages.get(player.getUniqueId());

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
