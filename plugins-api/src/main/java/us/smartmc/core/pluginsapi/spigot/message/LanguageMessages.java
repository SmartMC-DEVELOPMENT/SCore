package us.smartmc.core.pluginsapi.spigot.message;

import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.language.MultiLanguageRegistry;
import org.bukkit.entity.Player;

public class LanguageMessages extends MultiLanguageRegistry {

    // Variable to know if the registry has been created
    private static boolean alreadyCreated = false;
    private static LanguageMessages languageMessages;
    public static String NAME = "language";

    public static void setup() {
        if (alreadyCreated) return;
        new LanguageMessages();
    }

    public LanguageMessages() {
        super(NAME, holder -> {
            holder.load();

            holder.registerDefault("set_lang_title", "Change language");
            holder.remove("selected");

            holder.registerDefault("menu_previous", "Previous");
            holder.registerDefault("menu_close", "Close");

            for (Language language : Language.values()) {
                holder.registerDefault("lang_" + language.name() + "_name", "LANG:" + language.name());
                holder.registerDefault("lang_" + language.name() + "_name", "LANG:" + language.name());
            }

            holder.save();
        });
        alreadyCreated = true;
        languageMessages = this;
    }

    public String getSetLangTitle(Player player) {
        Language language = PlayerLanguages.get(player.getUniqueId());
        return get(player, language, "set_lang_title");
    }

    public static LanguageMessages get() {
        return languageMessages;
    }
}
