package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.instance.backend.PlayerServerConnectionsHandler;
import me.imsergioh.pluginsapi.instance.backend.request.ServerRedirectionRequest;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.event.LoggedInProxyPlayerEvent;

import java.net.InetSocketAddress;
import java.util.*;

public class ServerRedirectionsListeners {

    @Subscribe
    public void onLogin(LoggedInProxyPlayerEvent event) {
        Player player = event.getPlayer();

        Optional<InetSocketAddress> ipOptional = player.getVirtualHost();
        if (ipOptional.isEmpty()) return;
        InetSocketAddress address = ipOptional.get();

        FilePluginConfig config = SmartCoreVelocity.getConfig();

        String hostname = address.getHostName().toLowerCase().replace(".", "-");

        if (!config.containsKey("serverRedirections") && config.get("serverRedirections", Document.class).containsKey(hostname)) return;
        List<String> servers = config.get("serverRedirections", Document.class).getList(hostname, String.class);
        if (servers == null) return;
        int randomIndex = new Random().nextInt(servers.size());
        String randomServerName = servers.get(randomIndex);

        Optional<RegisteredServer> serverOptional = VelocityPluginsAPI.proxy.getServer(randomServerName);

        if (serverOptional.isEmpty()) return;
        System.out.println("Redirecting by serverRedirections " + player.getUsername() + " to " + randomServerName);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                PlayerServerConnectionsHandler.get(player).unregisterCurrentConnectionRequests();
                PlayerServerConnectionsHandler.get(player).sendConnectionQueue(serverOptional.get(), 200, 50);
            }
        }, 1000);
    }

}
