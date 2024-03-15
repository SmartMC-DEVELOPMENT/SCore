package us.smartmc.serverhandler.manager;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import us.smartmc.serverhandler.ServerHandlerVelocity;

import java.net.InetSocketAddress;
import java.util.Optional;

public class VelocityServerManager {

    public static void register(String name, String hostname, int port) {
        Optional<RegisteredServer> optional = ServerHandlerVelocity.getProxy().getServer(name);
        ServerInfo serverInfo;
        if (optional.isPresent()) {
            serverInfo = optional.get().getServerInfo();
            // Return here if server already exists and has the same hostname & port
            if (serverInfo.getAddress().getHostName().equalsIgnoreCase(hostname) &&
                    serverInfo.getAddress().getPort() == port) return;
        }
        ServerInfo info = new ServerInfo(name, new InetSocketAddress(hostname, port));
        ServerHandlerVelocity.getProxy().registerServer(info);
        System.out.println("New server has been registered! " + name);
    }

}
