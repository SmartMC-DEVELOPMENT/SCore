package us.smartmc.core.backend;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class SendPlayerMessageCommand extends BackendCommandExecutor {

    public SendPlayerMessageCommand() {
        super("sendPlayerMsg");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        if (args.length == 0) return;
        String username = args[0];
        String message = label.replaceFirst(username + " ", "");
        Player player = getPlayerFromUsername(username);

        if (player == null) return;
        player.sendMessage(PaperChatUtil.parse(player, message));
    }

    public Player getPlayerFromUsername(String username) {
        return Bukkit.getPlayer(username);
    }

}
