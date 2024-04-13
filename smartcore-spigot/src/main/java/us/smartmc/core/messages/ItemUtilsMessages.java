package us.smartmc.core.messages;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "item_utils")
public class ItemUtilsMessages extends MultiLanguageRegistry {

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("close_item_name", "&cClose");
        holder.registerDefault("exit_item_name", "&cExit");
        holder.save();
    }
}
