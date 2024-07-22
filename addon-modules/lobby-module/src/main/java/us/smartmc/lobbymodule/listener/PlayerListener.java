package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.instance.ClickableComponent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.BukkitUtil;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.messages.LobbyMessages;
import us.smartmc.lobbymodule.util.DiscordLinkUtil;
import us.smartmc.lobbymodule.util.FireworkUtil;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.Arrays;
import java.util.List;

public class PlayerListener extends AddonListener implements Listener {

    @EventHandler
    public void sendJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            if (!player.isOnline()) return;
            player.sendMessage(PaperChatUtil.parse(player, "<lang.lobby.join_embedmessage>"));
            Sound sound = Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON;
            player.playSound(player.getLocation(), sound, 1, 1);
        }, 20 * 2);


    }

    @EventHandler
    public void removeLinkDiscordCode(PlayerQuitEvent event) {
        DiscordLinkUtil.clearLinkDiscordCode(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void allowInteractWrittenBook(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        if (!item.getType().equals(Material.WRITTEN_BOOK)) return;
        event.setCancelled(false);
    }

    @EventHandler
    public void sendSubliminalSoundForJoining(PlayerJoinEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 7.7F);
    }

    @EventHandler
    public void sendSubliminalFireworkForJoining(PlayerJoinEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        FireworkUtil.spawnCustomFirework(player, Arrays.asList(Color.WHITE, Color.AQUA), null, FireworkEffect.Type.BALL,
                false, false, 2, 4);
    }

    @EventHandler
    public void sendVipMessage(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();

        // Al entrar un jugador con rango
        if(!player.hasPermission("smartmc.vip")) return;

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            String playerNamePrefixMessage = ChatUtil.parse(player, "<rank><reset><name>");
            onlinePlayer.sendMessage(PaperChatUtil.parse(onlinePlayer, "<lang.lobby.join_message>", playerNamePrefixMessage));
        }
    }
}
