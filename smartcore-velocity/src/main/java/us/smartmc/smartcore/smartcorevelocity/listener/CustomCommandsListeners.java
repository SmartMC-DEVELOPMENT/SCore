package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;

public class CustomCommandsListeners {

    @Subscribe(order = PostOrder.LAST)
    public void onChatPlayer(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()) return;
        System.out.println("CommandExecuteEvent " + event.getCommand());

        String label = event.getCommand();

        for (CustomCommandsManager manager : CustomCommandsManager.getManagers()) {
            boolean executed =
                    manager.execute(event.getCommandSource(), label);
            if (executed) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        }
    }
}
