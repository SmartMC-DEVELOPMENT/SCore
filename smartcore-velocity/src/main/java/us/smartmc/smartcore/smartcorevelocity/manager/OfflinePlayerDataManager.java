package us.smartmc.smartcore.smartcorevelocity.manager;

import com.mongodb.client.MongoCollection;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.instance.OfflinePlayerData;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class OfflinePlayerDataManager {


    public OfflinePlayerDataManager() {
    }

    @Subscribe(order = PostOrder.LAST)
    public void join(ServerConnectedEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (OfflinePlayerData.get(event.getPlayer()) == null) new OfflinePlayerData(event.getPlayer().getUniqueId());
                get(event.getPlayer()).parse(event.getPlayer());
            }
        }, 250);
    }

    @Subscribe
    public void quit(DisconnectEvent event) {
        OfflinePlayerData.removeCache(event.getPlayer().getUniqueId());
    }

    public static OfflinePlayerData get(Player player) {
        return OfflinePlayerData.get(player);
    }

    public static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("offline_player_data");
    }

}
