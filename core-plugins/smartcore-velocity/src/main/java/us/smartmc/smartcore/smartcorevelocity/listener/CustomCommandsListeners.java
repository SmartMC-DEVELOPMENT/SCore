package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;

public class CustomCommandsListeners {

    @Subscribe(order = PostOrder.LATE)
    public void onChatPlayer(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()) return;
        CustomCommandsManager.forEach(manager -> {
            boolean executed =
                    manager.execute(event.getCommandSource(), event.getCommand());
            if (executed) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        });
    }
}
