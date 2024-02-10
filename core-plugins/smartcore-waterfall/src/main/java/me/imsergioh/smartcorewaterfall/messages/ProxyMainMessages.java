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

            holder.registerDefault("no_permission", "&cYou don't have permission to do that!");
            holder.registerDefault("few_arguments_command_error", "&cYou've introduced too few arguments.");
            holder.registerDefault("too_much_arguments_command_error", "&cYou've introduced too much arguments.");
            holder.registerDefault("no_player_error", "&cThe player you're looking for doesn't exist.");
            holder.registerDefault("invalid_subcommand", "&cYou've introduced an invalid subcommand");
            holder.registerDefault("servers_not_found", "&cWe have not found servers to connect to!");

            holder.save();
        });
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }

}
