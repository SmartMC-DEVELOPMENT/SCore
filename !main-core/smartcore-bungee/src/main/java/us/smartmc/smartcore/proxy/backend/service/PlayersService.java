package us.smartmc.smartcore.proxy.backend.service;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.services.IBackendService;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import us.smartmc.smartcore.proxy.backend.SendVelocityMessageCommand;
import us.smartmc.smartcore.proxy.listener.BackendEssentialListeners;

import java.util.UUID;

public class PlayersService implements IBackendService {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    private boolean loaded;

    @Override
    public void load() {
        loaded = true;
        ConnectionInputManager.registerCommands(new SendVelocityMessageCommand());
        BungeeCordPluginsAPI.proxy.getPluginManager().registerListener(plugin, new BackendEssentialListeners());
    }

    @Override
    public void unload() {
        loaded = false;
    }

    public void registerPlayerContext(ProxiedPlayer player) {
        UUID id = player.getUniqueId();
        String context = getPlayerContext(id);
        SmartCoreBungeeCord.getBackendClient().subscribeContext(context);
    }

    public void unregisterPlayerContext(ProxiedPlayer player) {
        UUID id = player.getUniqueId();
        String context = getPlayerContext(id);
        SmartCoreBungeeCord.getBackendClient().unsubscribeContext(context);
    }

    public static String getPlayerContext(UUID id) {
        return "proxyPlayer@" + id;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
