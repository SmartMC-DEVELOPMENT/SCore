package us.smartmc.lobbycosmetics.instance.cosmetic;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.instance.player.ICosmeticPlayerSession;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;
import us.smartmc.lobbycosmetics.util.ItemsUtil;

public interface ICosmetic {

    default ItemBuilder getPreviewItemBuilder(ICosmeticPlayerSession session, ICosmeticSection<?> section, Language language) {
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("cosmetic_" + getId(), language, CosmeticsSectionMessages.getName(section.getId()));
        Material material = getMaterialType();
        String skullTexture = getSkullTexture();
        ItemBuilder builder = ItemBuilder.of(material);
        if (skullTexture != null) {
            builder = ItemBuilder.of(Material.SKULL_ITEM).data(3);
            builder.skullTexture(skullTexture);
        }
        return builder.name(info.getName()).lore(ItemsUtil.getBuilderCosmeticItemDescription(session, info, this));
    }

    void onEnableEntity(LivingEntity entity);
    void onDisableEntity(LivingEntity entity);

    void toggleEntity(LivingEntity entity);

    String getSkullTexture();

    CosmeticType getType();

    Material getMaterialType();

    default int getCost() {
        return 0;
    }

    default CosmeticRarity getRarity() {
        return CosmeticRarity.COMMON;
    }

    String getId();
}
