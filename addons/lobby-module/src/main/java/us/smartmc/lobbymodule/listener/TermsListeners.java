package us.smartmc.lobbymodule.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.HashMap;
import java.util.UUID;

public class TermsListeners extends AddonListener implements Listener {

    private static final HashMap<UUID, UUID> toAcceptPlayers = new HashMap<>();

    public static UUID getValidationTerms(Player player) {
        return toAcceptPlayers.get(player.getUniqueId());
    }

    public static void acceptTerms(CorePlayer player) {
        player.getPlayerData().setData("accepted_terms", System.currentTimeMillis() / 1000);
        toAcceptPlayers.remove(player.getUUID());
    }

    public static void declineTerms(CorePlayer player) {
        Player bukkitPlayer = player.get();
        player.getPlayerData().getDocument().remove("accepted_terms");
        openTermsBook(bukkitPlayer, "decline");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvOpen(InventoryOpenEvent event) {
        if (!isEnabled()) return;

        Player player = (Player) event.getPlayer();
        if (hasToAcceptTerms(CorePlayer.get(player))) {
            event.setCancelled(true);
            openTermsBook(player, "inventoryopen");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;

        Player player = event.getPlayer();
        if (hasToAcceptTerms(CorePlayer.get(player))) {
            event.setCancelled(true);
            openTermsBook(player, "interact");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelChat(AsyncChatEvent event) {
        if (!isEnabled()) return;

        Player player = event.getPlayer();
        if (hasToAcceptTerms(CorePlayer.get(player))) {
            event.setCancelled(true);
            openTermsBook(player, "chat");
        }
    }

    @EventHandler
    public void setupTerms(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        long accepted = 0;


        System.out.println("doc=" + event.getData().getDocument());
        try {
            accepted = event.getData().getDocument().get("accepted_terms", Number.class).longValue();
        } catch (Exception ignore) {}
        long lastModificationTimestamp = 1710788768;

        // Return if accepted before modification of terms
        System.out.println("accepted=" + accepted + " last=" + lastModificationTimestamp);
        if (accepted > lastModificationTimestamp) return;

        UUID uuid = UUID.randomUUID();
        toAcceptPlayers.put(player.getUniqueId(), uuid);
        openTermsBook(player, "dataloaded");
    }

    private static void openTermsBook(Player player, String reason) {
        String acceptId = toAcceptPlayers.get(player.getUniqueId()).toString();
        Book book = getBookTermsAndConditions(acceptId);
        player.openBook(book);
        System.out.println("opentermsbook " + reason);
    }

    public static boolean hasToAcceptTerms(CorePlayer corePlayer) {
        CorePlayerData data = corePlayer.getPlayerData();
        if (data == null) return false;
        return toAcceptPlayers.containsKey(corePlayer.getUUID());
    }

    private static Book getBookTermsAndConditions( String acceptID) {
        Book.Builder builder = Book.builder()
                .author(Component.text("Administration"));

        builder.addPage(Component.text("Hello!\n" +
                "§r\n" +
                "Welcome to SmartMC Network!\n" +
                "§r\n" +
                "Accept the terms and conditions to be able to play and enjoy our services and our Minecraft Network :D\n" +
                "§r\n")
                        .append(Component.text("§9§nRead terms here§r\n§r\n").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://smartmc.us/terms")))
                .append(Component.text("    §a§lACCEPT ").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/terms accept " + acceptID)))
                .append(Component.text("§c§lDENY    ")).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/terms deny " + acceptID)));
        return builder.build();
    }

}
