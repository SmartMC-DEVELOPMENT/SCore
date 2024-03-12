package us.smartmc.serverhandler.request;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;
import us.smartmc.serverhandler.ServerHandlerVelocity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUnregisterRequest implements Runnable {

    public static final Set<ServerUnregisterRequest> requests = new HashSet<>();
    private static final ProxyServer proxy = ServerHandlerVelocity.getProxy();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String name;
    private final RegisteredServer server;
    private ServerInfo fallbackServer = null;

    private final Set<UUID> connecting = new HashSet<>();

    public ServerUnregisterRequest(String name) {
        this.name = name;
        this.server = proxy.getServer(name).get();
        requests.add(this);
        findFallback();
        executor.submit(this);
    }

    private void findFallback() {
        String serverProfileName = name.replaceAll("[0-9]", "");
        for (RegisteredServer server : proxy.getAllServers()) {
            String serverName = server.getServerInfo().getName();
            if (serverName.startsWith(serverProfileName) && !serverName.equals(name)) {
                fallbackServer = server.getServerInfo();
                break;
            }
        }
    }

    @Override
    public void run() {
        if (fallbackServer == null) {
            server.getPlayersConnected().forEach(player -> player.disconnect(Component.text("Disconnected from the server. F in the chat")));
            proxy.unregisterServer(server.getServerInfo());
            executor.shutdown(); // Properly shutdown the executor
            return;
        }

        // Transfer players to fallback server
        long startRedirect = -1;
        for (Player player : server.getPlayersConnected()) {
            player.createConnectionRequest(proxy.getServer(fallbackServer.getName()).get());
            connecting.add(player.getUniqueId());
            if (startRedirect == -1) startRedirect = System.currentTimeMillis();
        }

        if (startRedirect == -1) startRedirect = System.currentTimeMillis();
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
        complete();
    }

    private void complete() {
        requests.remove(this);
        proxy.unregisterServer(proxy.getServer(name).get().getServerInfo());
        executor.shutdown();
    }

    public Set<UUID> getConnecting() {
        return connecting;
    }
}
