package us.smartmc.snowgames.messages;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

import java.util.List;

@LangMessagesInfo(name = "snowgames/ffa/main")
public class PluginMessages extends MultiLanguageRegistry {

    public static final String NAME = "snowgames/ffa/main";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("error_cant_join", "&cError while trying to join to the ffa game");
        holder.registerDefault("cant_join_spawn_ingame", "&cYou can't join at spawn in-game!");
        holder.registerDefault("kill_messages_list", List.of("&bEl jugador &c{0}&b ha sido asesinado por &a{1}&b!"));
        holder.registerDefault("death_messages_list", List.of("&bEl jugador &c{0}&b ha muerto!"));

        holder.registerDefault("not_available", "This feature is currently not available.");
        holder.registerDefault("tops_your_position", "Your position: &e{0}");
        holder.save();
    }
}
