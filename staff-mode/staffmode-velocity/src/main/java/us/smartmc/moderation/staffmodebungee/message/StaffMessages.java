package us.smartmc.moderation.staffmodebungee.message;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "staff-mode/bungee-main")
public class StaffMessages extends MultiLanguageRegistry {

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("chat.visibility-enabled", "&bStaffChat visibility enabled!");
        holder.registerDefault("chat.visibility-disabled", "&bStaffChat visibility disabled!");
        holder.registerDefault("chat.enabled", "&bStaffChat enabled!");
        holder.registerDefault("chat.disabled", "&bStaffChat disabled!");
        holder.registerDefault("chat.visibility-not-enabled", "&cTo send messages in the chat you can't have visibility disabled!");

        holder.save();
    }
}
