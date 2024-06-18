package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.event.LoggedInProxyPlayerEvent;
import us.smartmc.smartcore.smartcorevelocity.manager.HostnameRulesManager;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.List;

public class HostnameRedirectionsListeners {

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
        List<String> bossbars = HostnameRulesManager.getBossbarTexts(hostname);
        if (bossbars.isEmpty()) return;
        for (String text : bossbars) {
            Component bossbarComponent = VelocityChatUtil.parse(text);
            player.showBossBar(BossBar.bossBar(bossbarComponent, 1.0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS, Set.of()));
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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                player.createConnectionRequest(serverOptional.get()).fireAndForget();
            }
        }, 865);
    }

}
