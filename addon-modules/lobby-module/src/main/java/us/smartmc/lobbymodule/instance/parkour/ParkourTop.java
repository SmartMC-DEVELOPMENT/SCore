package us.smartmc.lobbymodule.instance.parkour;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bson.Document;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.lobbymodule.LobbyModule;

public class ParkourTop {

    private static final int TOP_LIMIT = 10;
    private static final String DATABASE = "player_data";
    private static final String COLLECTION = "core_players";
    private static final String BEST_TIME_KEY = "lobby.parkour.bestTime";

    private static boolean alreadySet;

    public static void setup() {
        if (alreadySet) return;
        showAt(LocationSerializer.toLocation(LobbyModule.getMainConfig().getString("top_parkour_location")));
        alreadySet = true;
    }

    private static void showAt(Location location) {
        HologramHolder holder = HologramHolder.getOrCreate("parkour_tops");
        holder.registerHologram("top_parkour", location, getTopText());
        holder.loadHologram("top_parkour", new HologramHolderConfig(holder));
    }


    private static String getTopText() {
        StringBuilder stringBuilder = new StringBuilder("<lang.lobby.parkour_top_title>");
        stringBuilder.append("\n");
        MongoCursor<Document> cursor = getTopDocuments();

        int currentTop = 1;
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String uuid = document.getString("_id");

            long millis = document
                    .get("lobby", Document.class)
                    .get("parkour", Document.class)
                    .get("bestTime", Number.class)
                    .longValue();

            double seconds = millis / 1000.0;
            String formattedTime = String.format("%.2f", seconds);
            stringBuilder.append(ChatUtil.parse("<lang.lobby.parkour_top_score>", currentTop, getNameFromId(uuid), formattedTime));
            stringBuilder.append("\n");
            currentTop++;
        }
        return stringBuilder.toString();
    }

    private static MongoCursor<Document> getTopDocuments() {
        return MongoDBConnection.mainConnection
                .getDatabase(DATABASE)
                .getCollection(COLLECTION)
                .find(Filters.exists(BEST_TIME_KEY))
                .sort(Sorts.ascending(BEST_TIME_KEY))
                .limit(TOP_LIMIT)
                .iterator();
    }

    private static String getNameFromId(String uuid) {
        Document document = MongoDBConnection.mainConnection
                .getDatabase("player_data")
                .getCollection("offline_player_data")
                .find(new Document("_id", uuid)).first();
        if (document == null) return null;
        return document.get("name", String.class);
    }

}
