package us.smartmc.smartcore.smartcorevelocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.manager.ServersHandler;

public class LobbyCommand extends CoreCommand {

    public LobbyCommand() {
        super("lobby");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        if (ServersHandler.isAtLobbyServer(player)) {
            return;
        }
        ServersHandler.connectToHubByHubRules(player);
    }
}
