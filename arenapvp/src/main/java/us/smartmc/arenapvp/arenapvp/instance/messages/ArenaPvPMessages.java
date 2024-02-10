
package us.smartmc.arenapvp.arenapvp.instance.messages;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.message.BukkitLangMessages;

@LangMessagesInfo(name = "game/arenapvp")
public class ArenaPvPMessages extends BukkitLangMessages {

    private static final ArenaPvPMessages INSTANCE = new ArenaPvPMessages();

    @Override
    public void load(LanguageMessagesHolder holder) {
        registerTitle(holder, "start_cancelled", "&c&lSTART CANCELLED", "&r");
    }

    public static ArenaPvPMessages get() {
        return INSTANCE;
    }
}
