package us.smartmc.npcsmodule.messages;


import us.smartmc.core.pluginsapi.language.MultiLanguageRegistry;

public class PluginMessages extends MultiLanguageRegistry {

    public PluginMessages() {
        super("npcs-module", holder -> {
            holder.registerDefault("online_player", "Player");
            holder.registerDefault("online_players", "Players");
            holder.save();
        });
    }
}
