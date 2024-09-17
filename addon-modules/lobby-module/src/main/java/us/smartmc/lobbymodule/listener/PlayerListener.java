package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.util.ChatUtil;
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
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.util.DiscordLinkUtil;
import us.smartmc.lobbymodule.util.FireworkUtil;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.Arrays;

public class PlayerListener extends AddonListener implements Listener {

    @EventHandler
    public void sendJoinMessage(PlayerDataLoadedEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) return;
        player.sendMessage(ChatUtil.parse(player, "<lang.lobby.join_embedmessage>", player.getName()).replace("{0}", player.getName()));
        Sound sound = Sound.CLICK;
        player.playSound(player.getLocation(), sound, 1, 1);
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
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 7.7F);
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
            String playerNamePrefixMessage = ChatUtil.parse(player, "<rank>&r<name>");
            onlinePlayer.sendMessage(ChatUtil.parse(onlinePlayer, "<lang.lobby.join_message>", playerNamePrefixMessage));
        }
    }
}
