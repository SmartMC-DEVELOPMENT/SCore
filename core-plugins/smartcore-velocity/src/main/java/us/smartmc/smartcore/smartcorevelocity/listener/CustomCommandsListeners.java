package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;

public class CustomCommandsListeners {

    @Subscribe
    public void onChatPlayer(PlayerChatEvent event) {
        String message = event.getMessage();
        CustomCommandsManager.forEach(manager -> {
            boolean executed =
                    manager.execute(event.getPlayer(), message);

            if (executed) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
            }
        });
    }
}
