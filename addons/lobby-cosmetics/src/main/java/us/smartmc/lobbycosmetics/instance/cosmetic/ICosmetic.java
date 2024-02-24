package us.smartmc.lobbycosmetics.instance.cosmetic;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;

public interface ICosmetic {

    default ItemBuilder getPreviewItemBuilder(ICosmeticSection<?> section, Language language) {
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("cosmetic_" + getId(), language, CosmeticsSectionMessages.getName(section.getId()));
        Material material = getMaterialType();
        String skullTexture = getSkullTexture();
        ItemBuilder builder = ItemBuilder.of(material);
        if (skullTexture != null) {
            builder = ItemBuilder.of(Material.SKULL_ITEM).data(3);
            builder.skullTexture(skullTexture);
        }
        return builder.name(info.getName()).lore(info.getDescription());
    }

    String getSkullTexture();

    CosmeticType getType();

    Material getMaterialType();

    String getId();
}
