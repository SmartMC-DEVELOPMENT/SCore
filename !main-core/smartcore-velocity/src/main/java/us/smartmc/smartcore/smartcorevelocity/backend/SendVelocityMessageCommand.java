package us.smartmc.smartcore.smartcorevelocity.backend;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.List;
import java.util.Optional;
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

        Component component = VelocityChatUtil.parse(player, message, messageArgs);

        player.sendMessage(component);
    }

    public Player getPlayerFromId(UUID id) {
        Optional<Player> oPlayer = VelocityPluginsAPI.proxy.getPlayer(id);
        return oPlayer.orElse(null);
    }

    public Player getPlayerFromUsername(String username) {
        Optional<Player> oPlayer = VelocityPluginsAPI.proxy.getPlayer(username);
        return oPlayer.orElse(null);
    }

}
