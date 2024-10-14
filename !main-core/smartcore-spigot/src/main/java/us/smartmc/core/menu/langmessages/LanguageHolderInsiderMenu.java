package us.smartmc.core.menu.langmessages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageHolderInsiderMenu extends GUIMenu {

    private final LanguageMessagesHolder defaultLangHolder;

    public LanguageHolderInsiderMenu(Player player, String name) {
        super(player, 9 * 6, "LanguageHolder Insider of " + name);
        defaultLangHolder = LanguagesHandler.get(Language.getDefault()).get(name);
    }

    @Override
    public void load() {
        int slot = 0;
        for (String key : defaultLangHolder.keySet()) {
            String path = "<lang." + defaultLangHolder.getName() + "." + key + ">";
            set(slot, ItemBuilder.of(Material.ARROW).name(key).lore().get(), "message " + path);
            slot++;
        }
    }

    private static String parseContent(Object content) {
        String stringContent = content.toString();

        if (content instanceof String) {
            stringContent = (String) content;
        }

        if (content instanceof List<?> list) {
            stringContent = Arrays.toString(list.toArray());
        }

        if (content instanceof Document document) {
            stringContent = document.toString();
        }

        String regex = "\\{(\\d+)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringContent);

        StringBuffer result = new StringBuffer();

        // Iterar sobre todos los matches encontrados
        while (matcher.find()) {
            // Reemplazar {n} por (arg n)
            String replacement = String.format("(arg %s)", matcher.group(1));
            matcher.appendReplacement(result, replacement);
        }
        return result.toString();
    }

}
