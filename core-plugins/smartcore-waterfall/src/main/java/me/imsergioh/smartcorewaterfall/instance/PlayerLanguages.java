
package me.imsergioh.smartcorewaterfall.instance;

import us.smartmc.core.pluginsapi.connection.MongoDBConnection;

import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLanguages {

    private static final HashMap<String, Language> langSetMap = new HashMap<>();

    public static void sendMessage(ProxiedPlayer player, String holder, String messagePath, Object... args) {
        String message = ChatUtil.parse(
                LanguagesHandler.get(getLanguage(player)).get(holder).getString(messagePath),
                args);
        player.sendMessage(message);
    }

    public static Language getLanguage(Object object) {
       String id = null;
        if (object instanceof ProxiedPlayer) {
            id = ((ProxiedPlayer) object).getUniqueId().toString();
        } else if (object instanceof String) {
            id = (String) object;
        }
        if (id == null) return Language.getDefault();

        if (langSetMap.containsKey(id)) return langSetMap.get(id);
        Document query = new Document().append("_id", id);
        Document document = MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("core_players")
                .find(query).first();
        if (document != null && document.containsKey("lang")) {
            try {
                Language language = Language.valueOf(document.getString("lang"));
                langSetMap.put(id, language);
                us.smartmc.core.pluginsapi.instance.PlayerLanguages.register(UUID.fromString(id), language);
                return language;
            } catch (Exception ignore) {
            }
        }
        return Language.getDefault();
    }

    public static void set(ProxiedPlayer player, Language language) {
        langSetMap.put(player.getUniqueId().toString(), language);
        us.smartmc.core.pluginsapi.instance.PlayerLanguages.register(player.getUniqueId(), language);
    }

    public static Language get(String id) {
        if (!langSetMap.containsKey(id))
            getLanguage(id);
        return langSetMap.get(id);
    }

}
