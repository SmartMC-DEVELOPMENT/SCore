package us.smartmc.lobbymodule.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.handler.LinkSocialsManager;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;
import us.smartmc.lobbymodule.messages.LobbyMessages;
import us.smartmc.lobbymodule.util.MenuUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LinkSocialsMenu extends CoreMenu {

    private UUID targetUUID;
    @Getter
    private String targetName;
    @Getter
    private Document document;

    @Getter
    private boolean showing;

    public LinkSocialsMenu(Player player) {
        super(player, 54, "<lang.lobby.menu_link_socials_title>");
        showing = false;
        setup();
    }

    public LinkSocialsMenu(Player player, String targetName) throws Exception {
        super(player, 54, "<lang.lobby.menu_show_socials_title>");
        Document query = new Document("lowercase_name", targetName.toLowerCase());
        Document targetDoc = MongoDBConnection.mainConnection
                .getDatabase("player_data").getCollection("offline_player_data").find(query).first();
        if (targetDoc == null) throw new Exception("No target found!");
        targetUUID = UUID.fromString(targetDoc.getString("_id"));
        this.targetName = targetDoc.getString("name");
        showing = true;
        setup();
    }

    public void setup() {
        loadDocument();
        ItemBuilder relleno = ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name(" ");
        MenuUtil.setBorder(relleno.get(), inventory);
        if (initCorePlayer.getPreviousOpenMenu() != null) {
            set(inventory.getSize() - 5, ItemBuilder.of(Material.BOOK).name("<lang.language.menu_previous>").get(initPlayer), "openPrevious");
        } else {
            set(8, ItemBuilder.of(Material.BARRIER).name("&c<lang.language.menu_close>").get(initPlayer), "closeInv");
        }

        register(11, LinkSocialType.YOUTUBE);
        register(13, LinkSocialType.TWITCH);
        register(15, LinkSocialType.TIKTOK);

        register(29, LinkSocialType.TWITTER);
        register(31, LinkSocialType.INSTAGRAM);
        register(33, LinkSocialType.DISCORD);

        register(43, LinkSocialType.GITHUB);

        document = null;
    }

    private void loadDocument() {
        if (targetUUID == null) {
            targetUUID = initPlayer.getUniqueId();
        }
        CorePlayer targetCorePlayer = CorePlayer.get(targetUUID);
        CorePlayerData data;
        if (targetCorePlayer != null) {
            data = targetCorePlayer.getPlayerData();
        } else {
            data = new CorePlayerData(targetUUID);
        }

        document = data.getDocument().get(LinkSocialsManager.DB_DOCUMENT_PATH, Document.class);
        if (document == null) document = new Document();
    }

    private void register(int slot, LinkSocialType type) {
        ItemStack initialItem = LobbyMessages.getItem(Material.PLAYER_HEAD, "link_social_network").get(initPlayer);
        String name = type.getDisplayName();
        List<String> lore = initialItem.getItemMeta().getLore();
        String labelCommand = "linkSocial " + type.name();

        String currentUser = document.containsKey(type.name()) ? document.getString(type.name()) : "none";
        LinkSocialAction action = LobbyModule.getLinkSocialsManager().get(type);
        String example = action == null ? "none" : action.getValidExample();

        if (showing) {
            lore = Arrays.asList(LobbyMessages.getLangList("link_socials_description_show"));
            labelCommand = "showSocial " + type.name() + " " + currentUser;
        }

        set(slot, ItemBuilder.of(Material.PLAYER_HEAD).data(3)
                .skullTexture(type.getSkullTexture())
                .name(name)
                .lore(lore, name, currentUser, example).get(initPlayer), labelCommand);
    }

    @Override
    public void load() {}
}

