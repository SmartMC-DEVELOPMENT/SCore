package us.smartmc.lobbymodule.listener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.instance.ClickableComponent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.BukkitUtil;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import us.smartmc.lobbymodule.messages.LobbyMessages;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.HashMap;
import java.util.UUID;

public class TermsListeners extends AddonListener implements Listener {

    private static final HashMap<UUID, UUID> toAcceptPlayers = new HashMap<>();

    private void acceptTerms(CorePlayer player) {
        player.getPlayerData().getDocument().put("accepted_terms", System.currentTimeMillis() / 1000);
        toAcceptPlayers.remove(player.getUUID());
    }

    private void declineTerms(CorePlayer player) {
        Player bukkitPlayer = player.get();
        player.getPlayerData().getDocument().remove("accepted_terms");
        UUID uuid = UUID.randomUUID();
        toAcceptPlayers.put(player.getUUID(), uuid);
        openTermsBook(bukkitPlayer, uuid.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvOpen(InventoryOpenEvent event) {
        if (!isEnabled()) return;
        Player player = (Player) event.getPlayer();
        if (!hasAcceptedTerms(CorePlayer.get(player))) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        if (!hasAcceptedTerms(CorePlayer.get(player))) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerCommandPreprocessEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        if (!toAcceptPlayers.containsKey(player.getUniqueId())) return;
        String message = event.getMessage();
        event.setCancelled(true);

        if (!message.contains("acceptTerms")) {
            declineTerms(CorePlayer.get(player));
            return;
        }
        message = message.replaceFirst("/", "");
        String[] args = event.getMessage().split(" ");
        if (args.length == 0) return;
        try {
            UUID commandUUID = UUID.fromString(args[1]);
            UUID uuid = toAcceptPlayers.get(player.getUniqueId());

            if (commandUUID.equals(uuid)) {
                event.setCancelled(true);
                acceptTerms(CorePlayer.get(player));
            }
        } catch (Exception ignore) {
        }
    }

    @EventHandler
    public void setupTerms(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        if (hasAcceptedTerms(event.getCorePlayer())) return;

        UUID uuid = UUID.randomUUID();
        toAcceptPlayers.put(player.getUniqueId(), uuid);
        openTermsBook(player, uuid.toString());
    }

    private void openTermsBook(Player player, String acceptID) {
        ItemStack book = LobbyMessages.getItem(Material.WRITTEN_BOOK, "terms").get(player);
        BookMeta bookMeta = getBookTermsAndConditions(player, book, acceptID);

        book.setItemMeta(bookMeta);

        int heldItemSlot = player.getInventory().getHeldItemSlot();
        ItemStack previousItem = player.getInventory().getItem(heldItemSlot);
        player.getInventory().setItem(heldItemSlot, book);
        openBookInHand(player);
        player.getInventory().setItem(heldItemSlot, previousItem);
    }

    private void openBookInHand(Player player)
    {
        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte)0);
        buf.writerIndex(1);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        CraftPlayer craftP = (CraftPlayer)player;
        craftP.getHandle().playerConnection.sendPacket(packet);
    }

    public static boolean hasAcceptedTerms(CorePlayer corePlayer) {
        return corePlayer.getPlayerData().getDocument().containsKey("accepted_terms");
    }

    private static BookMeta getBookTermsAndConditions(Player player, ItemStack book, String acceptID) {
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor("Administration");

        String text = "Hello!\n" +
                "&r\n" +
                "Welcome to SmartMC Network!\n" +
                "&r\n" +
                "Accept the terms and conditions to be able to play and enjoy our services and our Minecraft Network :D\n" +
                "&r\n";

        bookMeta.addPage(ChatUtil.parse(player, text));

        ClickableComponent webComponent = new ClickableComponent();
        webComponent.addURL("   &3&nRead terms here\n\n", "https://smartmc.us/terms");

        ClickableComponent acceptComponent = new ClickableComponent();
        acceptComponent.addRunCommand("   &a&nAccept&r    ", "/acceptTerms " + acceptID);

        ClickableComponent denyComponent = new ClickableComponent();
        denyComponent.addRunCommand("&4&nDecline", "/declineTerms");

        BukkitUtil.addComponentToBook(bookMeta, webComponent.getBuilder().create());
        BukkitUtil.addComponentToBook(bookMeta, acceptComponent.getBuilder().create());
        BukkitUtil.addComponentToBook(bookMeta, denyComponent.getBuilder().create());

        return bookMeta;
    }

}
