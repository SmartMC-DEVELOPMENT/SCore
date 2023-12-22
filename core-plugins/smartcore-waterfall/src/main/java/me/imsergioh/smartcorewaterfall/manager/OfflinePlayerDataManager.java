package me.imsergioh.smartcorewaterfall.manager;

import com.mongodb.client.MongoCollection;
import us.smartmc.core.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.smartcorewaterfall.instance.OfflinePlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;

public class OfflinePlayerDataManager implements Listener {

    private static MongoCollection<Document> colletion;

    public OfflinePlayerDataManager() {
        colletion = MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("offline_player_data");
    }

    @EventHandler
    public void join(ServerConnectEvent event) {
        if (OfflinePlayerData.get(event.getPlayer()) == null) new OfflinePlayerData(event.getPlayer().getUniqueId());
        get(event.getPlayer()).parse(event.getPlayer());
    }

    @EventHandler
    public void quit(PlayerDisconnectEvent event) {
        OfflinePlayerData data = get(event.getPlayer());
        data.parse(event.getPlayer());
        data.removeCache();
    }

    private static OfflinePlayerData get(ProxiedPlayer player) {
        return OfflinePlayerData.get(player);
    }

    public static MongoCollection<Document> getColletion() {
        return colletion;
    }
}
