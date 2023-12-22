package us.smartmc.lobbymodule.listener;

import org.bukkit.Sound;
import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.smartaddons.plugin.AddonListener;

public class PlayerListener extends AddonListener implements Listener {

    @EventHandler
    public void sendSubliminalSound(PlayerDataLoadedEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 7.7F);
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
}
