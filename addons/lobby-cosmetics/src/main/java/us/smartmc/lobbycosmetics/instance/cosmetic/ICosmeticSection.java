package us.smartmc.lobbycosmetics.instance.cosmetic;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;

import java.util.Collection;
import java.util.function.Consumer;

public interface ICosmeticSection<V extends ICosmetic> {

    void register(V... cosmetics);
    void unregister(String... ids);

    ICosmetic get(String id);
    void forEach(Consumer<V> consumer);

    default ItemBuilder getPreviewItemBuilder(Language language) {
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("section_" + getId().name(), language, CosmeticsMainMessages.NAME);
        Material material = getIconMaterial();
        String skullTexture = getSkullTexture();
        ItemBuilder builder = ItemBuilder.of(material);
        if (skullTexture != null) {
            builder = ItemBuilder.of(Material.SKULL_ITEM).data(3);
            builder.skullTexture(skullTexture);
        }
        return builder.name(info.getName()).lore(info.getDescription());
    }

    Collection<V> getCosmetics();

    Material getIconMaterial();

    default String getSkullTexture() {
        return null;
    }

    CosmeticType getId();
}
