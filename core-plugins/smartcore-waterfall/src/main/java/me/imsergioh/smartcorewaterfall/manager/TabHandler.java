package me.imsergioh.smartcorewaterfall.manager;

import us.smartmc.core.pluginsapi.instance.MongoDBPluginConfig;
import us.smartmc.core.pluginsapi.language.Language;
import net.md_5.bungee.api.plugin.Listener;
import org.bson.Document;

import java.util.HashMap;

public class TabHandler extends MongoDBPluginConfig implements Listener {

    private static final HashMap<Language, TabHandler> handlers = new HashMap<>();

    public static void register() {
        for (Language language : Language.values()) {
            new TabHandler(language);
        }
    }

    public TabHandler(Language language) {
        super("proxy_data", "tab_handler", new Document("lang", language.name().toUpperCase()));
        handlers.put(language, this);
        load();
        registerDefault("header", "Hello world!\nLine2!?");
        registerDefault("footer", "FOOTER HAHAHAHA");
        save();
    }

    public static HashMap<Language, TabHandler> getHandlers() {
        return handlers;
    }
}
