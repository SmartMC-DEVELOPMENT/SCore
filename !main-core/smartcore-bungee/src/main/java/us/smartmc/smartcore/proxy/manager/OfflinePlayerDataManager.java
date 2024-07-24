package us.smartmc.smartcore.proxy.manager;

import com.mongodb.client.MongoCollection;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;
import us.smartmc.smartcore.proxy.instance.OfflinePlayerData;

import java.util.Timer;
import java.util.TimerTask;

public class OfflinePlayerDataManager implements Listener {


    public OfflinePlayerDataManager() {
    }

    @EventHandler(priority = -1)
    public void join(ServerConnectedEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (OfflinePlayerData.get(event.getPlayer()) == null) new OfflinePlayerData(event.getPlayer().getUniqueId());
                get(event.getPlayer()).parse(event.getPlayer());
            }
        }, 250);
    }

    @EventHandler
    public void quit(PlayerDisconnectEvent event) {
        OfflinePlayerData.removeCache(event.getPlayer().getUniqueId());
    }

    public static OfflinePlayerData get(ProxiedPlayer player) {
        return OfflinePlayerData.get(player);
    }

    public static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("offline_player_data");
    }

}
