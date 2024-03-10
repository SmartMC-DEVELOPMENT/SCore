package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.smartcorewaterfall.manager.ServersHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("lobby", null, "hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (ServersHandler.isAtBlockedServer(player)) {
            System.out.println("LOBBYCOMMAND -> IS AT BLOCKED SERVER");
            return;
        }
        if (ServersHandler.isAtLobbyServer(player)) {
            System.out.println("LOBBYCOMMAND -> IS AT LOBBY SERVER");
            return;
        }
        ServersHandler.connectToHubByHubRules(player);
    }
}
