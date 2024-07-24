package us.smartmc.smartcore.proxy.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.instance.CoreCommand;
import us.smartmc.smartcore.proxy.manager.ServersHandler;

public class LobbyCommand extends CoreCommand {

    public LobbyCommand() {
        super("lobby");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) return;
        if (ServersHandler.isAtLobbyServer(player)) {
            return;
        }
        ServersHandler.connectToHubByHubRules(player);
    }

    @Override
    public String[] getAliases() {
        return new String[]{"hub", "vestibulo", "vestíbulo", "l"};
    }
}
