package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;
import us.smartmc.lobbymodule.linksocials.*;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

import java.util.*;

public class LinkSocialsManager extends ManagerRegistry<LinkSocialType, LinkSocialAction> implements Listener {

    public static final String DB_DOCUMENT_PATH = "socials";

    private final Map<UUID, LinkSocialType> pendingLinks = new HashMap<>();

    @Override
    public void load() {
        register(YouTubeLink.class,
                TwitchLink.class,
                TikTokLink.class,
                TwitterLink.class,
                InstagramLink.class,
                GitHubLink.class,
                DiscordLink.class);
        LobbyModule.getPlugin().registerListeners(SmartCore.getPlugin(), this);
    }

    @Override
    public void unload() {
    }

    public void perform(Player player, String message) {
        LinkSocialType type = pendingLinks.get(player.getUniqueId());
        if (type == null) return;
        LinkSocialAction action = get(type);
        if (action == null) return;
        action.perform(SmartCorePlayer.get(player), message);
    }

    public void associate(CorePlayer player, String url) {
        LinkSocialType type = pendingLinks.remove(player.getUUID());
        Document document = player.getPlayerData().getDocument().get(DB_DOCUMENT_PATH, Document.class);
        if (document == null) document = new Document();
        document.put(type.name(), url);
        player.getPlayerData().getDocument().put(DB_DOCUMENT_PATH, document);
        player.get().sendMessage(PaperChatUtil.parse(player.get(), "<lang.lobby.link_socials_linked_correctly>"));
    }

    public void removeCurrentLink(CorePlayer player, LinkSocialType type) {
        Document document = player.getPlayerData().getDocument().get(DB_DOCUMENT_PATH, Document.class);
        if (document == null) document = new Document();
        document.remove(type.name());
        player.getPlayerData().getDocument().put(DB_DOCUMENT_PATH, document);
        player.get().sendMessage(PaperChatUtil.parse(player.get(), "<lang.lobby.link_socials_unlinked_correctly>"));
        player.get().closeInventory();
    }

    public void registerPendingLink(Player player, LinkSocialType type) {
        UUID uuid = player.getUniqueId();
        pendingLinks.put(uuid, type);

        player.closeInventory();
        player.sendMessage(PaperChatUtil.parse(player, "<lang.lobby.link_socials_introduce_url>"));

        // 2 minutes later removes cache
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                pendingLinks.remove(uuid);
            }
        }, 120L * 1000);
    }

    @SafeVarargs
    public final void register(Class<? extends LinkSocialAction>... actions) {
        for (Class<? extends LinkSocialAction> actionClass : actions) {
            LinkSocialAction action;
            try {
                action = actionClass.newInstance();
                register(action.getType(), action);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventHandler
    public void judgePendingLink(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (!pendingLinks.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);
        perform(player, message);
    }
}
