package me.imsergioh.smartcorewaterfall.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

public class ProxyMainMessages extends MultiLanguageRegistry {

    public static final String NAME = "proxy_main";

    public ProxyMainMessages() {
        super(NAME, holder -> {
            holder.load();
            holder.registerDefault("no_staff", "&cYou are not a staff!");
            holder.save();
        });
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }

}
