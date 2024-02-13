package us.smartmc.moderation.staffmodebungee.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.moderation.staffmodebungee.StaffModeMainBungee;
import us.smartmc.moderation.staffmodebungee.manager.StaffPlayerManager;
import us.smartmc.moderation.staffmodebungee.util.RegistrationUtil;

public class ManagersEvents implements Listener {

    private static final StaffPlayerManager playerManager = StaffModeMainBungee.getPlayerManager();

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!player.hasPermission(RegistrationUtil.STAFF_PERMISSION)) return;
        playerManager.register(player);
    }

    @EventHandler
    public void onDisconnect(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        playerManager.unregister(player);
    }
}
