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

        // Regex para identificar segmentos con o sin códigos de color seguido de placeholders de mensajes localizados
        Pattern pattern = Pattern.compile("(?:(&[0-9a-fk-or]))?(<lang\\.(.*?)\\.(.*?)>)");
        Matcher matcher = pattern.matcher(message);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // Obtener el código de color, si está presente
            String colorCode = matcher.group(1) != null ? ChatColor.translateAlternateColorCodes('&', matcher.group(1)) : "";
            // Identificar las partes del placeholder del mensaje localizado
            String messageHolder = matcher.group(3);
            String path = matcher.group(4);

            // Obtener el mensaje localizado
            String localizedMessage = getLocalizedMessage(language, messageHolder, path);

            // Preparar el segmento con el mensaje localizado, aplicando el código de color si está presente
            String replacement = colorCode + localizedMessage;

            // Reemplazar en el resultado, asegurándose de manejar correctamente el carácter $ y las referencias de grupo
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        // Devolver el resultado final, no es necesario aplicar translateAlternateColorCodes aquí
        // ya que todos los códigos de color se aplican individualmente a cada segmento localizado
        return result.toString();
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
