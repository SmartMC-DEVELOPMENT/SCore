package us.smartmc.serverhandler.request;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.serverhandler.ServerHandlerMain;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUnregisterRequest implements Runnable {

    public static final Set<ServerUnregisterRequest> requests = new HashSet<>();
    private static final ProxyServer proxy = ServerHandlerMain.getPlugin().getProxy();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String name;
    private final ServerInfo server;
    private ServerInfo fallbackServer = null;

    private final Set<UUID> connecting = new HashSet<>();

    public ServerUnregisterRequest(String name) {
        this.name = name;
        this.server = proxy.getServerInfo(name);
        requests.add(this);
        findFallback();
        executor.submit(this);
    }


    private void findFallback() {
        String serverProfileName = name.replaceAll("[0-9]", "");
        for (String serverName : proxy.getServersCopy().keySet()) {
            if (serverName.startsWith(serverProfileName) && !serverName.equals(name)) {
                fallbackServer = proxy.getServerInfo(serverName);
                break;
            }
        }
    }

    @Override
    public void run() {
        if (fallbackServer == null) {
            server.getPlayers().forEach(player -> player.disconnect("Disconnected from the server. F in the chat"));
            proxy.getServers().remove(name);
            executor.shutdown(); // Properly shutdown the executor
            return;
        }

        // Transfer players to fallback server
        long startRedirect = -1;
        for (ProxiedPlayer player : server.getPlayers()) {
            player.connect(fallbackServer);
            connecting.add(player.getUniqueId());
            if (startRedirect == -1) startRedirect = System.currentTimeMillis();
        }

        if (startRedirect == -1) return;
        while (!connecting.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long pastTime = System.currentTimeMillis() - startRedirect;
            if (pastTime >= 5000) {
                complete();
                return;
            }
        }
    }

    private void complete() {
        requests.remove(this);
        proxy.getServers().remove(name);
        executor.shutdown();
    }

    public Set<UUID> getConnecting() {
        return connecting;
    }
}
