package us.smartmc.smartcore.smartcorevelocity.backend.service;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.service.BackendService;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.backend.SendVelocityMessageCommand;
import us.smartmc.smartcore.smartcorevelocity.listener.BackendEssentialListeners;

import java.util.UUID;

public class PlayersService extends BackendService {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    @Override
    public void load() {
        super.load();
        ConnectionInputManager.registerCommands(new SendVelocityMessageCommand());
        VelocityPluginsAPI.proxy.getEventManager().register(plugin, new BackendEssentialListeners());
    }

    @Override
    public void unload() {
        super.unload();
    }

    public void registerPlayerContext(Player player) {
        UUID id = player.getUniqueId();
        String context = getPlayerContext(id);
        BackendClient.mainConnection.subscribeContext(context);
    }

    public void unregisterPlayerContext(Player player) {
        UUID id = player.getUniqueId();
        String context = getPlayerContext(id);
        BackendClient.mainConnection.unsubscribeContext(context);
    }

    public static String getPlayerContext(UUID id) {
        return "proxyPlayer@" + id;
    }


}
