package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;

import java.util.List;

public abstract class CosmeticsAddonMenu extends CoreMenu implements ICosmeticAddonMenu {

    protected final Language language;

    public CosmeticsAddonMenu(Player player, int size, String menuId) {
        super(player, size, "<lang." + CosmeticsMainMessages.NAME + ".menu_" + menuId + ".title>");
        this.language = PlayerLanguages.get(player.getUniqueId());
    }

    @Override
    public void load() {
        registerDefaults();
    }

    protected void registerSection(int slot, CosmeticType type) {
        ItemBuilder builder = LobbyCosmetics.getSectionsHandler().get(type).getPreviewItemBuilder(language);
        inventory.setItem(slot, builder.get(player));
        actionManager.registerItemAction(slot, get(slot), List.of("openCosmeticSection " + type.name()));
    }
}
