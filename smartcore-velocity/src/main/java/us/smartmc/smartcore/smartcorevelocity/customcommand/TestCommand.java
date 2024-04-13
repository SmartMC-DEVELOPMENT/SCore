package us.smartmc.smartcore.smartcorevelocity.customcommand;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.instance.commandmanager.CustomCommandExecutor;

public class TestCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        sender.sendMessage(Component.text("Test custom command executed!"));
    }
}
