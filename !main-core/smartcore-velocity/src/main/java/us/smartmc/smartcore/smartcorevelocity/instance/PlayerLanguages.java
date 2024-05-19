
package us.smartmc.smartcore.smartcorevelocity.instance;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLanguages {

    private static final HashMap<UUID, Language> langSetMap = new HashMap<>();

    public static void register(UUID uuid, Language language) {
        langSetMap.put(uuid, language);
    }

    public static void sendMessage(Player player, String holder, String messagePath, Object... args) {
        Component message = VelocityChatUtil.parse(
                LanguagesHandler.get(getLanguage(player)).get(holder).getString(messagePath),
                args);
        player.sendMessage(message);
    }

    public static Language getLanguage(UUID uuid) {
        Language language = langSetMap.get(uuid);
        return language == null ? Language.getDefault() : language;
    }

    public static Language getLanguage(Player player) {
        return getLanguage(player.getUniqueId());
    }

    public static void set(Player player, Language language) {
        langSetMap.put(player.getUniqueId(), language);
        me.imsergioh.pluginsapi.instance.PlayerLanguages.register(player.getUniqueId(), language);
    }
}
