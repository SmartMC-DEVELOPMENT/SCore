package us.smartmc.smartcore.smartcorevelocity.instance.commandmanager;

import com.velocitypowered.api.command.CommandSource;

public interface CustomCommandExecutor {

    void onCommand(CommandSource sender, String[] args);
}
