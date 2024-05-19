package us.smartmc.lobbycosmetics.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.lobbycosmetics.hatcosmetic.CakeHat;
import us.smartmc.lobbycosmetics.hatcosmetic.TestHat;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmeticSection;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;

import java.util.Arrays;
import java.util.List;

public class CosmeticSectionMenu extends CoreMenu {

    private static final CosmeticSessionHandler sessionsHandler = LobbyCosmetics.getPlayerSessionsHandler();

    private final CosmeticPlayerSession playerSession;
    private final Language language;
    @Getter
    private final ICosmeticSection<?> section;

    public CosmeticSectionMenu(Player player, ICosmeticSection<?> section) {
        super(player, 54, "<cosmetic_section_name_" + section.getId().name());
        this.playerSession = sessionsHandler.get(player.getUniqueId());
        this.language = PlayerLanguages.get(player.getUniqueId());
        this.section = section;
    }

    @Override
    public void load() {
        int slot = 0;
        for (ICosmetic cosmetic : LobbyCosmetics.getSectionsHandler().get(section.getId()).getCosmetics()) {
            registerCosmetic(slot, cosmetic.getId());
            slot++;
        }
    }

    protected void registerCosmetic(int slot, String id) {
        ICosmetic cosmetic = LobbyCosmetics.getSectionsHandler().get(section.getId()).get(id);
        cosmetic.getPreviewItemBuilder(playerSession, section, language);
        ItemBuilder builder = cosmetic.getPreviewItemBuilder(playerSession, section, language);
        inventory.setItem(slot, builder.get(initPlayer));
        actionManager.registerItemAction(slot, get(slot), Arrays.asList("toggleCosmetic " + cosmetic.getType().name() + " " + cosmetic.getId()));
    }
}
