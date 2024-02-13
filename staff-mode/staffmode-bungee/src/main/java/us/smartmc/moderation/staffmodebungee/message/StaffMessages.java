package us.smartmc.moderation.staffmodebungee.message;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "staff-mode/bungee-main")
public class StaffMessages extends MultiLanguageRegistry {

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("chat.enabled", "&fStaffChat visibility: &a&lENABLED");
        holder.registerDefault("chat.disabled", "&fStaffChat visibility: &c&lDISABLED");
    }
}
