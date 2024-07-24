package us.smartmc.smartcore.proxy.listener;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.kyori.adventure.bossbar.BossBar;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.event.LoggedInProxyPlayerEvent;
import us.smartmc.smartcore.proxy.manager.HostnameRulesManager;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.List;

public class HostnameRedirectionsListeners implements Listener {

    private static final Map<UUID, String> redirections = new HashMap<>();

    @EventHandler(priority = -1)
    public void cancelIfRedirecting(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!redirections.containsKey(event.getPlayer().getUniqueId())) return;
        String redirectionServerName = redirections.get(player.getUniqueId());
        String preServerName = player.getName();
        if (redirectionServerName.equals(preServerName)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void handleRedirections(LoggedInProxyPlayerEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player == null) return;

        InetSocketAddress address = player.getPendingConnection().getVirtualHost();
        String hostname = address.getHostName().toLowerCase().replace(".", "-");

        handleRedirection(player, hostname);
    }

    @EventHandler
    public void handleBossbar(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

        InetSocketAddress address = player.getPendingConnection().getVirtualHost();

        String hostname = address.getHostName().toLowerCase().replace(".", "-");

        handleBossbars(player, hostname);
    }

    public void handleBossbars(ProxiedPlayer player, String hostname) {
        Set<BossBar> bossBars = HostnameRulesManager.getBossbars(hostname);
        if (bossBars == null) return;

        for (BossBar bossBar : bossBars) {
            //player.hideBossBar(bossBar);
            //player.showBossBar(bossBar);
        }
    }


    public void handleRedirection(ProxiedPlayer player, String hostname) {
        List<String> servers = HostnameRulesManager.getHostnameRedirections(hostname);
        if (servers.isEmpty()) return;
        int randomIndex = new Random().nextInt(servers.size());
        String randomServerName = servers.get(randomIndex);

        ServerInfo serverInfo = BungeeCordPluginsAPI.proxy.getServerInfo(randomServerName);

        System.out.println("Redirecting by hostnameRedirections " + player.getName() + " to " + randomServerName);

        redirections.put(player.getUniqueId(), serverInfo.getName());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                player.connect(serverInfo);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        redirections.remove(player.getUniqueId());
                    }
                }, 1000);
            }
        }, 1000);
    }
}
