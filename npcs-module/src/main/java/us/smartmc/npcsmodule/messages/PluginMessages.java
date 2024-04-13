package us.smartmc.npcsmodule.messages;


import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "npcs-module")
public class PluginMessages extends MultiLanguageRegistry {

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("online_player", "Player");
        holder.registerDefault("online_players", "Players");
        holder.save();
    }
}
