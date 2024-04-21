package us.smartmc.smartcore.smartcorevelocity.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import org.bson.Document;

import java.util.HashMap;

public class TabHandler extends MongoDBPluginConfig {

    @Getter
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
}
