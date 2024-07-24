package us.smartmc.smartcore.proxy.backend;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.List;
import java.util.UUID;

public class SendVelocityMessageCommand extends BackendCommandExecutor {

    public SendVelocityMessageCommand() {
        super("sendVelocityMsg");
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

        ProxiedPlayer player;

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

        BaseComponent component = ChatUtil.parse(player, message, messageArgs);

        player.sendMessage(component);
    }

    public ProxiedPlayer getPlayerFromId(UUID id) {
        return BungeeCordPluginsAPI.proxy.getPlayer(id);
    }

    public ProxiedPlayer getPlayerFromUsername(String username) {
        return BungeeCordPluginsAPI.proxy.getPlayer(username);
    }

}
