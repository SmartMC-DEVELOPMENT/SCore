package us.smartmc.serverhandler;

import me.imsergioh.jbackend.BackendConnection;
import me.imsergioh.jbackend.api.manager.BackendActionManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import us.smartmc.serverhandler.listener.RequestsListeners;
import us.smartmc.serverhandler.registration.CommandsRegistration;
import us.smartmc.serverhandler.registration.CommonListenerRegistration;
import us.smartmc.serverhandler.util.ConnectionUtil;

public class ServerHandlerMain extends Plugin {

    public static ProxyServer proxy;

    private static ServerHandlerMain plugin;

    private static BackendConnection backendConnection;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();
        proxy = getProxy();

        Registrations.register(CommonListenerRegistration.class,
                CommandsRegistration.class);

        BackendActionManager.registerConnectAction(handler -> {
            ConnectionUtil.sendCommand(handler, "imAProxy");
        });

        backendConnection = new BackendConnection("localhost", 55777);
        backendConnection.start();

        proxy.getPluginManager().registerListener(this, new RequestsListeners());
    }

    public static BackendConnection getBackendConnection() {
        return backendConnection;
    }

    public static ServerHandlerMain getPlugin() {
        return plugin;
    }
}
