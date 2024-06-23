package us.smartmc.npcsmodule.messages;


import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "npcs-module")
public class PluginMessages extends MultiLanguageRegistry {

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("onlinePlayer", "Player");
        holder.registerDefault("onlinePlayers", "Players");
        holder.save();
    }
}
