package us.smartmc.lobbycosmetics.message;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmeticSection;
import us.smartmc.lobbycosmetics.instance.helper.MsgCosmeticMetaRegistror;

import java.util.HashMap;
import java.util.Map;

public class CosmeticsSectionMessages {

    private static final Map<CosmeticType, CosmeticsSectionMessages> messages = new HashMap<>();

    private final ICosmeticSection<?> section;
    @Getter
    private final CosmeticType type;

    protected CosmeticsSectionMessages(ICosmeticSection<?> section) {
        this.section = section;
        this.type = section.getId();
        loadDefaults();
    }

    public void loadDefaults() {
        for (Language language : Language.values()) {
            load(LanguagesHandler.get(language).get(CosmeticsSectionMessages.getName(section.getId())));
        }
    }

    public void load(LanguageMessagesHolder holder) {
        holder.load();
        for (ICosmetic cosmetic : section.getCosmetics()) {
            new MsgCosmeticMetaRegistror(section, cosmetic);
        }
        holder.save();
    }

    public static String getName(CosmeticType type) {
        return "cosmetics_info/" + type.name();
    }

    public static void registerMessages(ICosmeticSection<?> section) {
        new CosmeticsSectionMessages(section);
    }

    public static CosmeticsSectionMessages get(CosmeticType type) {
        CosmeticsSectionMessages msgs = messages.get(type);
        if (msgs == null) {
            CosmeticSection<?> section = LobbyCosmetics.getSectionsHandler().get(type);
            msgs = new CosmeticsSectionMessages(section);
            messages.put(type, msgs);
        }
        return msgs;
    }

}
