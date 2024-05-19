package us.smartmc.lobbycosmetics.instance.helper;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmeticSection;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;

public class MsgCosmeticMetaRegistror {

    private final ICosmetic cosmetic;

    public MsgCosmeticMetaRegistror(ICosmeticSection<?> section, ICosmetic cosmetic) {
        this.cosmetic = cosmetic;
        for (Language language : Language.values()) {
            register(LanguagesHandler.get(language).get(CosmeticsSectionMessages.getName(section.getId())));
        }
    }

    private void register(LanguageMessagesHolder holder) {
        String id = cosmetic.getId();
        holder.registerDefault("cosmetic_" + id + ".name", "Cosmetic " + cosmetic.getId());
        holder.registerDefault("cosmetic_" + id + ".description", "");
    }

}
