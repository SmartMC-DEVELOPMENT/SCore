package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        if (!message.contains("<lang.")) return message;
        System.out.println("LANG PARSE NORMAL -> " + message);
        return get(null, message);
    }

    @Override
    public String parse(Player player, String message) {
        if (player == null) return message;
        if (!message.contains("<lang.")) return message;
        return get(player, message);
    }

    private String get(Player player, String message) {
        // Obtener el idioma del jugador, o el idioma por defecto si player es null
        Language language = player != null ? PlayerLanguages.get(player.getUniqueId()) : Language.getDefault();

        while (message.contains("<lang.")) {
            
        }
    }

    private String getLocalizedMessage(Language language, String messageHolder, String path) {
        // Obtener el mensaje localizado basado en el idioma, messageHolder y path
        Object object = LanguagesHandler
                .get(language)
                .get(messageHolder)
                .get(path);

        if (object instanceof String) {
            return (String) object;
        } else {
            return "LANGUAGE_ERROR";
        }
    }
}
