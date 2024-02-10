package us.smartmc.gamesmanager.gamesmanagerspigot.message;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.message.BukkitLangMessages;

@LangMessagesInfo(name = "game/gamemanager")
public class GameManagerMessages extends BukkitLangMessages {

    private static final GameManagerMessages INSTANCE = new GameManagerMessages();

    public static BukkitLangMessages get() {
        return INSTANCE;
    }

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        registerTitle(holder, "starting", "&e&lSTARTING in &c&l{0}", "&aReady to fight?");
        holder.registerDefault("chat_message.starting", "&aThe Game will start in {0}s...");
        holder.save();
    }
}
