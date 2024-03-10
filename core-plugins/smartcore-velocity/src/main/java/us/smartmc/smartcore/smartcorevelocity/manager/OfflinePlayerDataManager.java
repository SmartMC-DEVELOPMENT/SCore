package us.smartmc.smartcore.smartcorevelocity.manager;

import com.mongodb.client.MongoCollection;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.instance.OfflinePlayerData;

public class OfflinePlayerDataManager {

    @Getter
    private static MongoCollection<Document> colletion;

    public OfflinePlayerDataManager() {
        colletion = MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("offline_player_data");

    }

    @Subscribe
    public void join(ServerPreConnectEvent event) {
        if (OfflinePlayerData.get(event.getPlayer()) == null) new OfflinePlayerData(event.getPlayer().getUniqueId());
        get(event.getPlayer()).parse(event.getPlayer());
    }

    @Subscribe
    public void quit(DisconnectEvent event) {
        OfflinePlayerData data = get(event.getPlayer());
        data.parse(event.getPlayer());
        data.removeCache();
    }

    public static OfflinePlayerData get(Player player) {
        return OfflinePlayerData.get(player);
    }
}
