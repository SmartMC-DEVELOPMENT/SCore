package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.util.FireworkUtil;
import us.smartmc.lobbymodule.util.PlayerUtil;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.List;

public class PlayerListener extends AddonListener implements Listener {

    @EventHandler
    public void sendSubliminalSoundForJoining(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 7.7F);
    }

    @EventHandler
    public void sendSubliminalFireworkForJoining(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FireworkUtil.spawnCustomFirework(player, List.of(Color.WHITE, Color.AQUA), null, FireworkEffect.Type.BALL,
                false, false, 2, 4);
    }

    @EventHandler
    public void sendVipMessage(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();

        // Al entrar un jugador con rango
        if(!player.hasPermission("smartmc.vip")) return;

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            String playerNamePrefixMessage = ChatUtil.parse(player, "<rank><name>");

            onlinePlayer.sendMessage(ChatUtil.parse(onlinePlayer, "<lang.lobby.join_message>", playerNamePrefixMessage));
        }
    }

    @EventHandler
    public void cancelInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (SmartCore.getPlugin().getAdminModeHandler().isActive(player)) return;
        event.setCancelled(true);
    }
}
