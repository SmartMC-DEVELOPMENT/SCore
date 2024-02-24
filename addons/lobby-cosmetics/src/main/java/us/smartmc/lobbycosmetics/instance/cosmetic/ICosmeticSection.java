package us.smartmc.lobbycosmetics.instance.cosmetic;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.message.CosmeticsInfoMessages;

public interface ICosmeticSection<V> {

    void register(V cosmetic);
    void unregister(String id);

    ICosmetic get(String id);

    default ItemBuilder getPreviewItemBuilder(Language language) {
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("section_" + getId().name(), language, CosmeticsInfoMessages.NAME);
        Material material = getIconMaterial();
        String skullTexture = getSkullTexture();
        ItemBuilder builder = ItemBuilder.of(material);
        if (skullTexture != null) {
            builder = ItemBuilder.of(Material.SKULL_ITEM);
            builder.skullTexture(skullTexture);
        }
        return builder.name(info.getName()).lore(info.getDescription());
    }

    Material getIconMaterial();

    default String getSkullTexture() {
        return null;
    }

    CosmeticActionType getId();
}
