package us.smartmc.lobbycosmetics.message;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bson.Document;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticRarity;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;

import java.util.Arrays;
import java.util.List;

@LangMessagesInfo(name = CosmeticsMainMessages.NAME)
public class CosmeticsMainMessages extends MultiLanguageRegistry {

    public static final String NAME = "cosmetics_info/main";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        registerSection(CosmeticType.UNKNOWN, "&8???", "Unknown description. Not type defined");
        registerSection(CosmeticType.HATS, "Hats", "Hats that make you look cool wihtruhrtegiuhegroihuewrgjhoi");
        registerSection(CosmeticType.EFFECTS, "Effects", "Default effects description to look if the item works correcttlly. extreeemeeee looong");
        registerSection(CosmeticType.PETS, "Pets", "Pets, no sé");

        holder.registerDefault("unlocked", "Unlocked");
        holder.registerDefault("rarity", "Rarity");
        holder.registerDefault("cost", "Cost");
        holder.registerDefault("cost_free", "Free");

        holder.registerDefault("click_to_look", "&eClick to look!");
        holder.registerDefault("click_to_buy", "&eClick to buy!");
        holder.registerDefault("click_to_set", "&eClick to set!");

        holder.registerDefault("menu_confirm_buy_title", "Confirm purchase?");
        holder.registerDefault("cosmetic_buyed_coins_reason", "Purchased cosmetic");
        holder.registerDefault("cosmetic_no_balance", "&cYou don't have enough SmartCoins!");
        holder.registerDefault("item_buy_name", "&aConfirm");
        holder.registerDefault("item_cancel_buy_name", "&cCancel");
        holder.registerDefault("cosmetic_set", "&bYou selected &a{0}");

        for (CosmeticRarity rarity : CosmeticRarity.values()) {
            holder.registerDefault("rarity_" + rarity.name() + "_name", "RARITY " + rarity.name());
        }

        holder.save();
    }

    public void registerSection(CosmeticType type, String name, String description) {
        registerConfigValues("section_" + type.name() + ".", new Document()
                .append("name", name)
                .append("description", description));
    }

    private void registerConfigValues(String pathPrefix, Document document) {
        String holderName = getName();
        for (Language language : Language.values()) {
            LanguageMessagesHolder holder = LanguagesHandler.get(language).get(holderName);
            boolean hadChanges = false;
            for (String key : document.keySet()) {
                String finalkey = pathPrefix + key;
                if (!hadChanges && holder.containsKey(finalkey)) hadChanges = true;
                holder.registerDefault(finalkey, document.get(key));
            }
            if (hadChanges) holder.save();
        }
    }

}
