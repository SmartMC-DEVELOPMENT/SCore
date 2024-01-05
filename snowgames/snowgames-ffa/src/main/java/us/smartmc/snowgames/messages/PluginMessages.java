package us.smartmc.snowgames.messages;

import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

public class PluginMessages extends MultiLanguageRegistry {

    public static final String NAME = "snowgames/ffa/main";

    public PluginMessages() {
        super(NAME, holder -> {
            holder.load();
            holder.registerDefault("error_cant_join", "&cError while trying to join to the ffa game");
            holder.registerDefault("cant_join_spawn_ingame", "&cYou can't join at spawn in-game!");
        });
    }
}
