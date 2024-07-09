package us.smartmc.serverhandler;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.serverhandler.listener.RequestsListeners;
import us.smartmc.serverhandler.registration.CommandsRegistration;
import us.smartmc.serverhandler.registration.CommonListenerRegistration;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "serverhandler-velocity",
        name = "serverhandler-velocity",
        version = "1.0-SNAPSHOT"
)
public class ServerHandlerVelocity {

    @Inject
    private Logger logger;

    @Getter
    private final Path dataDirectory;

    @Getter
    public static ProxyServer proxy;

    private static ServerHandlerVelocity plugin;

    private static BackendClient backendClient;

    @Inject
    public ServerHandlerVelocity(@DataDirectory Path dataDirectory, ProxyServer server) {
        proxy = server;
        this.dataDirectory = dataDirectory;
        dataDirectory.toFile().mkdirs();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;
        Registrations.register(
                CommonListenerRegistration.class,
                CommandsRegistration.class);

        try {
            backendClient = new BackendClient("localhost", 55777);
            backendClient.login("default", "SmartMC2024Ñ");
            new Thread(backendClient).start();
            backendClient.sendCommand("imAProxy");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        proxy.getEventManager().register(this, new RequestsListeners());
    }
}
