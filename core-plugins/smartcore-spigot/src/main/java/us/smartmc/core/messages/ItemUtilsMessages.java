package us.smartmc.core.messages;

import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

public class ItemUtilsMessages extends MultiLanguageRegistry {

    public ItemUtilsMessages() {
        super("item_utils", holder -> {
            holder.load();
            holder.registerDefault("close_item_name", "&cClose");
            holder.registerDefault("exit_item_name", "&cExit");
            holder.save();
        });
    }
}
