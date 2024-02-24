package us.smartmc.lobbycosmetics.util;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticRarity;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.instance.player.ICosmeticPlayerSession;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;

import java.util.ArrayList;
import java.util.List;

public class ItemsUtil {

    private static final MultiLanguageRegistry mainMessages = LobbyCosmetics.getCosmeticsMainMessages();

    public static List<String> getBuildedSectionItemDescription(Language language, MsgHolderLanguageInfo info, int unlocked, int total) {
        List<String> list = info.getDescription();

        String unlockedName = mainMessages.get(language, "unlocked");
        int percentage = 0;

        if (unlocked > 1) {
            percentage = 100 / total * unlocked;
        }

        list.addAll(List.of("&r", unlockedName + ": &c" + unlocked + "/" + total + " &8(" + percentage + "%)",
                "&r",
                "<lang." + CosmeticsMainMessages.NAME + ".click_to_look>"));

        list.replaceAll(s -> {
            if (!s.startsWith("&")) s = "&7" + s;
            return s;
        });

        return list;
    }

    public static List<String> getBuilderCosmeticItemDescription(ICosmeticPlayerSession session, MsgHolderLanguageInfo info, ICosmetic cosmetic) {
        List<String> list = new ArrayList<>(info.getDescription());

        Language language = PlayerLanguages.get(session.getId());
        CosmeticRarity rarity = cosmetic.getRarity();
        String rarityPrefix = mainMessages.get(language, "rarity");
        String rarityTextValue = "&" + rarity.getColor() + mainMessages.get(language, "rarity_" + cosmetic.getRarity() + "_name");
        list.add(rarityPrefix + ": " + rarityTextValue);

        // Check if player has cosmetic, if not then introduce formatted price lines
        boolean hasCosmetic = session.getData().hasCosmetic(cosmetic.getType(), cosmetic.getId());
        if (!hasCosmetic) {
            String pricePrefix = mainMessages.get(language, "cost");
            String priceTextValue = "&6" + (cosmetic.getCost() == 0 ? mainMessages.get(language, "cost_free") : String.valueOf(cosmetic.getCost()));
            list.addAll(List.of("&r", pricePrefix + ": " + priceTextValue));
        }

        String langClickPath = hasCosmetic ? "click_to_set" : "click_to_buy";

        list.add("&r");
        list.add("<lang." + CosmeticsMainMessages.NAME + "." + langClickPath + ">");

        list.replaceAll(s -> {
            if (!s.startsWith("&")) s = "&7" + s;
            return s;
        });

        return list;
    }

}
