package us.smartmc.smartcore.smartcorevelocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.manager.ServersHandler;

public class LobbyCommand extends CoreCommand {

    public LobbyCommand() {
        super("lobby", null, "hub");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
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
