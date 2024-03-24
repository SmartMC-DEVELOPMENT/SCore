package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.util.VariableUtil;

public class HexVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        // Primero, verificar si el mensaje contiene el patrón de color que buscamos
        if (!(message.contains("<#"))) return message;

        // Encuentra y reemplaza todos los patrones de color <#RRGGBB> por el formato admitido por Minecraft &#RRGGBB
        String transformedMessage = transformHexColorCodes(message);

        // No es necesario procesar con MiniMessage en este caso
        return transformedMessage;
    }

    @Override
    public String parse(Player player, String message) {
        return parse(message);
    }

    /**
     * Transforma los códigos de color hexadecimales del formato <#RRGGBB> al formato soportado (&#RRGGBB) y los aplica.
     *
     * @param message El mensaje original con códigos de color en formato <#RRGGBB>.
     * @return El mensaje con códigos de color transformados y aplicados.
     */
    private String transformHexColorCodes(String message) {
        // Regex para detectar el patrón <#RRGGBB>
        final String hexPattern = "<#([0-9a-fA-F]{6})>";

        // Buscar y reemplazar todos los patrones en el mensaje
        StringBuffer sb = new StringBuffer();
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(hexPattern).matcher(message);
        while (matcher.find()) {
            // Reemplazar el patrón encontrado por el equivalente de ChatColor
            matcher.appendReplacement(sb, ChatColor.of("#" + matcher.group(1)).toString());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
