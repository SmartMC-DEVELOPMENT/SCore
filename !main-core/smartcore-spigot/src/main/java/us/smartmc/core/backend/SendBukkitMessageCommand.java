package us.smartmc.core.backend;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.List;
import java.util.UUID;

public class SendBukkitMessageCommand extends BackendCommandExecutor {

    public SendBukkitMessageCommand() {
        super("sendBukkitMsg");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        if (args.length == 0) return;

        StringBuilder jsonBuilder = new StringBuilder();

        for (String arg : args) {
            jsonBuilder.append(arg).append(" ");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        String json = jsonBuilder.toString();
        Document document = Document.parse(json);

        Player player;

        try {
            UUID id = UUID.fromString(document.getString("_id"));
            player = getPlayerFromId(id);
        } catch (Exception e) {
            String name = document.getString("_id");
            player = getPlayerFromUsername(name);
        }
        String message = document.getString("message");

        if (player == null) return;

        List<?> argsList = document.get("args", List.class);
        Object[] messageArgs = argsList != null ? argsList.toArray() : new Object[0];

        String parsedMessage = ChatUtil.parse(player, message, messageArgs);

        player.sendMessage(parsedMessage);
    }

    public Player getPlayerFromId(UUID id) {
        return Bukkit.getPlayer(id);
    }

    public Player getPlayerFromUsername(String username) {
        return Bukkit.getPlayer(username);
    }

}
