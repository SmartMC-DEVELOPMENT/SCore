
package us.smartmc.smartcore.proxy.instance;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLanguages {

    private static final HashMap<UUID, Language> langSetMap = new HashMap<>();

    public static void register(UUID uuid, Language language) {
        langSetMap.put(uuid, language);
    }

    public static void sendMessage(ProxiedPlayer player, String holder, String messagePath, Object... args) {
        BaseComponent message = ChatUtil.parse(
                LanguagesHandler.get(getLanguage(player)).get(holder).getString(messagePath),
                args);
        player.sendMessage(message);
    }

    public static Language getLanguage(UUID uuid) {
        Language language = langSetMap.get(uuid);
        return language == null ? Language.getDefault() : language;
    }

    public static Language getLanguage(ProxiedPlayer player) {
        return getLanguage(player.getUniqueId());
    }

    public static void set(ProxiedPlayer player, Language language) {
        langSetMap.put(player.getUniqueId(), language);
        me.imsergioh.pluginsapi.instance.PlayerLanguages.register(player.getUniqueId(), language);
    }
}
