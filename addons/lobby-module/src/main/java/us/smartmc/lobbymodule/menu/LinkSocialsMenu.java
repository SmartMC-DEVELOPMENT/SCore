package us.smartmc.lobbymodule.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.handler.LinkSocialsManager;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;
import us.smartmc.lobbymodule.messages.LobbyMessages;
import us.smartmc.lobbymodule.util.MenuUtil;

import java.util.List;
import java.util.UUID;

public class LinkSocialsMenu extends CoreMenu {

    private final String targetName;
    private Document document;

    @Getter
    private boolean showing;

    public LinkSocialsMenu(Player player) {
        super(player, 54, "<lang.lobby.menu_link_socials_title>");
        targetName = player.getName();
    }

    public LinkSocialsMenu(Player player, String targetName) {
        super(player, 54, "<lang.lobby.menu_show_socials_title>");
        this.targetName = targetName;
        showing = true;
    }

    @Override
    public void load() {
        loadDocument();
        ItemBuilder relleno = ItemBuilder.of(Material.STAINED_GLASS_PANE).data(3).name(" ");
        MenuUtil.setBorder(relleno.get(), inventory);
        if (corePlayer.getPreviousOpenMenu() != null) {
            set(inventory.getSize() - 5, ItemBuilder.of(Material.BOOK).name("<lang.language.menu_previous>").get(player), "openPrevious");
        } else {
            set(8, ItemBuilder.of(Material.BARRIER).name("&c<lang.language.menu_close>").get(player), "closeInv");
        }

        register(10, LinkSocialType.YOUTUBE);
        register(13, LinkSocialType.TWITCH);
        register(15, LinkSocialType.TIKTOK);

        register(29, LinkSocialType.TWITTER);
        register(31, LinkSocialType.INSTAGRAM);
        register(33, LinkSocialType.DISCORD);

        register(43, LinkSocialType.GITHUB);
    }

    private void loadDocument() {
        SmartCorePlayer targetCorePlayer = SmartCorePlayer.get(targetName);
        document = targetCorePlayer.getPlayerData().getDocument().get(LinkSocialsManager.DB_DOCUMENT_PATH, Document.class);
        if (document == null) document = new Document();
    }

    private void register(int slot, LinkSocialType type) {
        ItemStack initialItem = LobbyMessages.getItem(Material.SKULL_ITEM, "link_social_network").get(player);
        String name = ChatUtil.parse(player, type.getDisplayName());
        List<String> lore = initialItem.getItemMeta().getLore();
        String currentLinkSet = document.containsKey(type.name()) ? document.getString(type.name()) : "none";
        LinkSocialAction action = LobbyModule.getLinkSocialsManager().get(type);
        String example = action == null ? "none" : action.getValidExample();

        set(slot, ItemBuilder.of(Material.SKULL_ITEM).data(3)
                .skullTexture(type.getSkullTexture())
                .name(name)
                .lore(lore, name, currentLinkSet, example).get(player), "linkSocial " + type.name());
    }
}
