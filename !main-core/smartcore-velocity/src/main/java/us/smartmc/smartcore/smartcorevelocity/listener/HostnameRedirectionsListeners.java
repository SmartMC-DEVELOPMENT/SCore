package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import net.kyori.adventure.bossbar.BossBar;
import us.smartmc.smartcore.smartcorevelocity.event.LoggedInProxyPlayerEvent;
import us.smartmc.smartcore.smartcorevelocity.manager.HostnameRulesManager;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.List;

public class HostnameRedirectionsListeners {

    private static final Map<UUID, String> redirections = new HashMap<>();

    @Subscribe(order = PostOrder.LAST)
    public void cancelIfRedirecting(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        if (!redirections.containsKey(player.getUniqueId())) return;
        String redirectionServerName = redirections.get(player.getUniqueId());
        String preServerName = event.getOriginalServer().getServerInfo().getName();
        if (redirectionServerName.equals(preServerName)) return;

        event.setResult(ServerPreConnectEvent.ServerResult.denied());
    }

    @Subscribe
    public void handleRedirections(LoggedInProxyPlayerEvent event) {
        Player player = event.getPlayer();

        Optional<InetSocketAddress> ipOptional = player.getVirtualHost();
        if (ipOptional.isEmpty()) return;
        InetSocketAddress address = ipOptional.get();

        String hostname = address.getHostName().toLowerCase().replace(".", "-");

        handleRedirection(player, hostname);
    }

    @Subscribe
    public void handleBossbar(ServerConnectedEvent event) {
        Player player = event.getPlayer();

        Optional<InetSocketAddress> ipOptional = player.getVirtualHost();
        if (ipOptional.isEmpty()) return;
        InetSocketAddress address = ipOptional.get();

        String hostname = address.getHostName().toLowerCase().replace(".", "-");

        handleBossbars(player, hostname);
    }

    public void handleBossbars(Player player, String hostname) {
        Set<BossBar> bossBars = HostnameRulesManager.getBossbars(hostname);
        if (bossBars == null) return;

        for (BossBar bossBar : bossBars) {
            player.hideBossBar(bossBar);
            player.showBossBar(bossBar);
        }
    }


    public void handleRedirection(Player player, String hostname) {
        List<String> servers = HostnameRulesManager.getHostnameRedirections(hostname);
        if (servers.isEmpty()) return;
        int randomIndex = new Random().nextInt(servers.size());
        String randomServerName = servers.get(randomIndex);

        Optional<RegisteredServer> serverOptional = VelocityPluginsAPI.proxy.getServer(randomServerName);

        if (serverOptional.isEmpty()) return;
        System.out.println("Redirecting by hostnameRedirections " + player.getUsername() + " to " + randomServerName);

        redirections.put(player.getUniqueId(), serverOptional.get().getServerInfo().getName());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                player.createConnectionRequest(serverOptional.get()).fireAndForget();
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
