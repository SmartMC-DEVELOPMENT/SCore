package us.smartmc.lobbycosmetics.instance.cosmetic;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.LineLimiter;
import org.bukkit.Material;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;
import us.smartmc.lobbycosmetics.util.ItemsUtil;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface ICosmeticSection<V extends ICosmetic> {

    void register(V... cosmetics);
    void unregister(String... ids);

    ICosmetic get(String id);
    void forEach(Consumer<V> consumer);

    default ItemBuilder getPreviewItemBuilder(Language language, int unlocked, int total) {
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("section_" + getId().name(), language, CosmeticsMainMessages.NAME);
        Material material = getIconMaterial();
        String skullTexture = getSkullTexture();
        ItemBuilder builder = ItemBuilder.of(material);
        if (skullTexture != null) {
            builder = ItemBuilder.of(Material.PLAYER_HEAD).data(3);
            builder.skullTexture(skullTexture);
        }
        return builder.name(info.getName()).lore(ItemsUtil.getBuildedSectionItemDescription(language, info, unlocked, total));
    }

    Collection<V> getCosmetics();

    Material getIconMaterial();

    default String getSkullTexture() {
        return null;
    }

    CosmeticType getId();
}
