package us.smartmc.gamesmanager.message;

import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

public class GameMessages extends MultiLanguageRegistry {
    public GameMessages() {
        super("gamesManager/game", holder -> {
            holder.load();
            holder.put("game_starting_in", "&eGame will start in &c{0}s&e...");
            holder.put("game_started", "&c&lGAME STARTED!");
            holder.put("player_joined", "&b{0}&e has joined (&b{1}&e/&b{2}&e)!");
            holder.put("player_quited", "&b{0}&e has quited (&b{1}&e/&b{2}&e)!");
            holder.put("death_title", "&c&lYOU DIED!");
            holder.put("death_subtitle", "&7Next time it will be better");
            holder.save();
        });
    }

    public static String getMessageVariable(String path) {
        return "<lang.gamesManager/game." + path + ">";
    }

}
