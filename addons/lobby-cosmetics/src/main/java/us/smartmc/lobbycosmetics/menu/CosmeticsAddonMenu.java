package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import org.bson.Document;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.handler.CosmeticSectionHandler;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;

import java.util.Arrays;
import java.util.List;

public abstract class CosmeticsAddonMenu extends CoreMenu implements ICosmeticAddonMenu {

    private static final CosmeticSectionHandler sectionsHandler = LobbyCosmetics.getSectionsHandler();
    private static final CosmeticSessionHandler sessionsHandler = LobbyCosmetics.getPlayerSessionsHandler();

    private final CosmeticPlayerSession playerSession;
    protected final Language language;

    public CosmeticsAddonMenu(Player player, int size, String menuId) {
        super(player, size, getTitle(player, menuId));
        this.playerSession = sessionsHandler.get(player.getUniqueId());
        this.language = PlayerLanguages.get(player.getUniqueId());
    }

    @Override
    public void load() {
        registerDefaults();
    }

    protected void registerSection(int slot, CosmeticType type) {
        int unlocked = playerSession.getData().getUnlocked(type);

        ItemBuilder builder = LobbyCosmetics.getSectionsHandler().get(type).getPreviewItemBuilder(language, unlocked, sectionsHandler.get(type).getCosmetics().size());
        inventory.setItem(slot, builder.get(language));
        actionManager.registerItemAction(slot, get(slot), Arrays.asList("openCosmeticSection " + type.name()));
    }

    private static String getTitle(Player player, String id) {
        Language language = PlayerLanguages.get(player.getUniqueId());
        LanguageMessagesHolder holder = LanguagesHandler.get(language).get(CosmeticsMainMessages.NAME);
        return holder.get("menu_" + id, Document.class).getString("title");
    }

}
