package us.smartmc.smartcore.proxy.backend.service;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.service.BackendService;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import us.smartmc.smartcore.proxy.backend.SendVelocityMessageCommand;
import us.smartmc.smartcore.proxy.listener.BackendEssentialListeners;

import java.util.UUID;

public class PlayersService extends BackendService {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    @Override
    public void load() {
        super.load();
        ConnectionInputManager.registerCommands(new SendVelocityMessageCommand());
        BungeeCordPluginsAPI.proxy.getPluginManager().registerListener(plugin, new BackendEssentialListeners());
    }

    @Override
    public void unload() {
        super.unload();
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


}
