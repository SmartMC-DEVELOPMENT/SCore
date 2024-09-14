package us.smartmc.smartcore.proxy.variables;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.language.Language;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class LanguageVariables extends VariableListener<ProxiedPlayer> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(ProxiedPlayer player, String message) {
        if (player == null) return message;
        if (!message.contains("<lang.")) return message;
        try {
            return get(player, message);
        } catch (Exception e) {
            return message;
        }
    }

    private String get(ProxiedPlayer player, String message) {
        Language language = Language.getDefault();
        if (player != null) language = me.imsergioh.pluginsapi.instance.PlayerLanguages.get(player.getUniqueId());

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

            if (object == null) return message;

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
