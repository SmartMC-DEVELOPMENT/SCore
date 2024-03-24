package us.smartmc.lobbymodule.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
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
        player.getPlayerData().getDocument().put("accepted_terms", System.currentTimeMillis() / 1000);
        toAcceptPlayers.remove(player.getUUID());
    }

    public static void declineTerms(CorePlayer player) {
        Player bukkitPlayer = player.get();
        player.getPlayerData().getDocument().remove("accepted_terms");
        openTermsBook(bukkitPlayer);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvOpen(InventoryOpenEvent event) {
        if (!isEnabled()) return;

        Player player = (Player) event.getPlayer();
        if (hasToAcceptTerms(CorePlayer.get(player))) {
            event.setCancelled(true);
            openTermsBook(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;

        Player player = event.getPlayer();

        if (hasToAcceptTerms(CorePlayer.get(player))) {
            event.setCancelled(true);
            openTermsBook(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelChat(AsyncChatEvent event) {
        if (!isEnabled()) return;

        Player player = event.getPlayer();
        if (hasToAcceptTerms(CorePlayer.get(player))) {
            event.setCancelled(true);
            openTermsBook(player);
        }
    }

    @EventHandler
    public void setupTerms(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        if ( CorePlayer.get(player).getPlayerData().getDocument().containsKey("accepted_terms")) return;
        UUID uuid = UUID.randomUUID();
        toAcceptPlayers.put(player.getUniqueId(), uuid);
        openTermsBook(player);
    }

    private static void openTermsBook(Player player) {
        String acceptId = toAcceptPlayers.get(player.getUniqueId()).toString();
        Book book = getBookTermsAndConditions(acceptId);
        player.openBook(book);
    }

    public static boolean hasToAcceptTerms(CorePlayer corePlayer) {
        return toAcceptPlayers.get(corePlayer.getUUID()) != null;
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
                .append(Component.text("§a§lACCEPT ").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/terms accept " + acceptID)))
                .append(Component.text("§c§lDENY")).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/terms deny " + acceptID)));
        return builder.build();
    }

}
