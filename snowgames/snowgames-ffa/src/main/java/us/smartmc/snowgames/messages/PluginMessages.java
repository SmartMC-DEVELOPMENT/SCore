package us.smartmc.snowgames.messages;

import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

import java.util.List;

public class PluginMessages extends MultiLanguageRegistry {

    public static final String NAME = "snowgames/ffa/main";

    public PluginMessages() {
        super(NAME, holder -> {
            holder.load();
            holder.registerDefault("error_cant_join", "&cError while trying to join to the ffa game");
            holder.registerDefault("cant_join_spawn_ingame", "&cYou can't join at spawn in-game!");
            holder.registerDefault("kill_messages_list", List.of("&bEl jugador &c{0}&b ha sido asesinado por &a{1}&b!"));
            holder.registerDefault("death_messages_list", List.of("&bEl jugador &c{0}&b ha muerto!"));

            holder.registerDefault("not_available", "This feature is currently not available.");

            holder.save();
        });
    }
}
