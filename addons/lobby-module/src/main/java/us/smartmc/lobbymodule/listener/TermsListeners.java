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
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import us.smartmc.lobbymodule.messages.LobbyMessages;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TermsListeners extends AddonListener implements Listener {

    private static final Set<UUID> toAcceptPlayers = new HashSet<>();

    private void acceptTerms(CorePlayer player) {
        player.getPlayerData().setData("accepted_terms", true);
    }

    private void declineTerms(CorePlayer player) {
        Player bukkitPlayer = player.get();
        player.getPlayerData().setData("accepted_terms", false);
        openTermsBook(bukkitPlayer);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        if (!toAcceptPlayers.contains(player.getUniqueId())) return;
        event.setCancelled(true);
        String message = event.getMessage();
        if (message.equals("acceptTerms")) {
            acceptTerms(CorePlayer.get(player));
        } else {
            declineTerms(CorePlayer.get(player));
        }
    }

    @EventHandler
    public void setupTerms(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        if (hasAcceptedTerms(event.getCorePlayer())) return;
        toAcceptPlayers.add(player.getUniqueId());

        openTermsBook(player);
    }

    private void openTermsBook(Player player) {
        ItemStack book = LobbyMessages.getItem(Material.WRITTEN_BOOK, "terms").get(player);
        BookMeta bookMeta = getBookTermsAndConditions(player, book);

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

    private static boolean hasAcceptedTerms(CorePlayer corePlayer) {
        return corePlayer.getPlayerData().getDocument().containsKey("accepted_terms") ?
                corePlayer.getPlayerData().getDocument().getBoolean("accepted_terms") :
                false;
    }

    private static BookMeta getBookTermsAndConditions(Player player, ItemStack book) {
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor("Administration");

        String text = """
                Hello, <name>!
                &r
                Welcome to SmartMC Network!
                &r
                Accept the terms and conditions to be able to play and enjoy our services and our Minecraft Network :D
                &r
                """;

        bookMeta.addPage(ChatUtil.parse(player, text));

        ClickableComponent webComponent = new ClickableComponent();
        webComponent.addURL("   &3&nRead terms here\n\n", "https://www.youtube.com/watch?t=105&v=6rAo_W4BWRo&feature=youtu.be");

        ClickableComponent acceptComponent = new ClickableComponent();
        acceptComponent.addRunCommand("   &a&nAccept&r    ", "acceptTerms");

        ClickableComponent denyComponent = new ClickableComponent();
        denyComponent.addRunCommand("&4&nDecline", "declineTerms");

        BukkitUtil.addComponentToBook(bookMeta, webComponent.getBuilder().create());
        BukkitUtil.addComponentToBook(bookMeta, acceptComponent.getBuilder().create());
        BukkitUtil.addComponentToBook(bookMeta, denyComponent.getBuilder().create());

        return bookMeta;
    }

}
