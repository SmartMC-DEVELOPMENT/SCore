package us.smartmc.serverhandler.manager;

import net.md_5.bungee.api.config.ServerInfo;
import us.smartmc.serverhandler.ServerHandlerMain;

import java.net.InetSocketAddress;

public class BungeeServerManager {

    public static void register(String name, String hostname, int port) {
        // Return here if server already exists and has the same hostname & port
        if (ServerHandlerMain.proxy.getServerInfo(name) != null &&
        ServerHandlerMain.proxy.getServerInfo(name).getAddress().getHostName().equalsIgnoreCase(hostname) &&
        ServerHandlerMain.proxy.getServerInfo(name).getAddress().getPort() == port) return;

        ServerInfo info = ServerHandlerMain.proxy.constructServerInfo(name, new InetSocketAddress(hostname, port), "A registered server by ServerHandler", false);
        ServerHandlerMain.proxy.getServers().put(info.getName(), info);
        System.out.println("New server has been registered! " + name);
    }

}
