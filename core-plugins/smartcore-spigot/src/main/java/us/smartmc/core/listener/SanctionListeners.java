package us.smartmc.core.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SanctionListeners implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void chat(AsyncChatEvent event) {
        String message = event.message().insertion();
        assert message != null;
        if (message.startsWith("/")) return;
        Player player = event.getPlayer();
        event.setCancelled(!getActiveMutes(player.getUniqueId()).isEmpty());
    }

    public Collection<Document> getActiveMutes(UUID uuid) {
        List<Document> list = new ArrayList<>();
        Document query = new Document("_id", uuid.toString())
                .append("type", "MUTE");
        for (Document document : MongoDBConnection.mainConnection
                .getDatabase("proxy_data")
                .getCollection("player_sanctions")
                .find(query)) {
            if (!document.containsKey("expiration_at")) {
                list.add(document);
                break;
            }
            long expiration = document.getLong("expiration_at");
            if (System.currentTimeMillis() < expiration) list.add(document);
        }
        return list;
    }

}
