package us.smartmc.core.backend;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class SendPlayerMessageCommand extends BackendCommandExecutor {

    public SendPlayerMessageCommand() {
        super("sendPlayerMsg");
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
        System.out.println("JSON=" + json);
        Document document = org.bson.Document.parse(json);
        UUID id = UUID.fromString(document.getString("_id"));
        String message = document.getString("message");

        Player player = getPlayerFromId(id);
        if (player == null) return;

        Object[] messageArgs = Arrays.stream(new ArrayList[]{document.get("args", ArrayList.class)}).toArray();
        Component component = PaperChatUtil.parse(player, message, messageArgs);
        player.sendMessage(component);
    }

    public Player getPlayerFromId(UUID id) {
        return Bukkit.getPlayer(id);
    }

    public Player getPlayerFromUsername(String username) {
        return Bukkit.getPlayer(username);
    }

}
