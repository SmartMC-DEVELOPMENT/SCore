package us.smartmc.core.backend.service;

import org.bukkit.entity.Player;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.service.BackendService;
import us.smartmc.backend.service.players.common.PlayersServiceContexts;
import us.smartmc.core.SmartCore;
import us.smartmc.core.backend.SendBukkitMessageCommand;

import java.util.UUID;

public class PlayersService extends BackendService {

    @Override
    public void load() {
        super.load();
        ConnectionInputManager.registerCommands(new SendBukkitMessageCommand());
    }

    @Override
    public void unload() {
        super.unload();
    }

    public void registerPlayerContext(Player player) {
        UUID id = player.getUniqueId();
        String context = PlayersServiceContexts.getPlayerContext(id);
        SmartCore.getPlugin().getBackendClient().subscribeContext(context);
    }

    public void unregisterPlayerContext(Player player) {
        UUID id = player.getUniqueId();
        String context = PlayersServiceContexts.getPlayerContext(id);
        SmartCore.getPlugin().getBackendClient().unsubscribeContext(context);
    }

}
