package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;
import us.smartmc.lobbymodule.linksocials.YouTubeLink;

import java.util.*;

public class LinkSocialsManager extends ManagerRegistry<LinkSocialType, LinkSocialAction> implements Listener {

    private static final String DATA_LINK_DOCUMENT_NAME = "socials";

    private final Map<UUID, LinkSocialType> pendingLinks = new HashMap<>();

    @Override
    public void load() {
        register(new YouTubeLink());
    }

    @Override
    public void unload() {
    }

    public void perform(Player player, String message) {
        LinkSocialType type = pendingLinks.get(player.getUniqueId());
        if (type == null) return;
        get(type).perform(SmartCorePlayer.get(player), message);
    }

    public void associate(CorePlayer player, String url) {
        LinkSocialType type = pendingLinks.remove(player.getUUID());
        Document document = player.getPlayerData().getDocument().get(DATA_LINK_DOCUMENT_NAME, Document.class);
        if (document == null) document = new Document();
        document.put(type.name(), url);
        player.get().sendMessage(ChatUtil.parse(player.get(), "&a<lang.lobby.link_socials_linked_correctly>"));
    }

    public void registerPendingLink(Player player, LinkSocialType type) {
        UUID uuid = player.getUniqueId();
        pendingLinks.put(uuid, type);

        player.closeInventory();
        player.sendMessage(ChatUtil.parse(player, "<lang.lobby.link_socials_introduce_url>"));

        // 20 seconds removes cache
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                pendingLinks.remove(uuid);
            }
        }, 20L * 1000);
    }

    public void register(LinkSocialAction... actions) {
        for (LinkSocialAction action : actions) {
            register(action.getType(), action);
        }
    }

    @EventHandler
    public void judgePendingLink(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (!pendingLinks.containsKey(player.getUniqueId())) return;
        perform(player, message);
    }

}
