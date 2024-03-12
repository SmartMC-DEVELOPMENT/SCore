package us.smartmc.security.antiddos.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.security.antiddos.handler.ConnectionHandler;

public class SmartAntiDDoSCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        if (sender.hasPermission("*")) {
            ConnectionHandler.toggleNotifications((Player) sender);
        }
    }
}
