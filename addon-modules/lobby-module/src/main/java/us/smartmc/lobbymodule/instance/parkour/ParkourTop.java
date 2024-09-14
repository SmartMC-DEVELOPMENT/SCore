package us.smartmc.lobbymodule.instance.parkour;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramBuilder;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.lobbymodule.LobbyModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ParkourTop {

    private static final int TOP_LIMIT = 10;
    private static final String DATABASE = "player_data";
    private static final String COLLECTION = "core_players";
    private static final String BEST_TIME_KEY = "lobby.parkour.bestTime";

    private static boolean alreadySet;

    private static Hologram hologram;
    private static Timer timer;

    public static void setup() {
        if (alreadySet) return;
        showAt(LocationSerializer.toLocation(LobbyModule.getMainConfig().getString("top_parkour_location")));
        alreadySet = true;
    }

    private static void showAt(Location location) {
        if (timer != null) return;
        timer = new Timer();
        hologram = HologramBuilder.create("top_parkour", location)
                .registerToHolder(HologramHolder.getOrCreate("main"))
                .lines("<lang.lobby.parkour_top_title>")
                .build();
        getTopsLines().forEach(hologram::addLine);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTop(hologram);
            }
        }, 100, 3000);
    }

    private static void updateTop(Hologram hologram) {
        List<String> topLines = getTopsLines();

        // Start by index 1 (because the title is 0) & updateLine by his index getting -1 index of iteration that corresponds the correct index top
        for (int index = 1; index < TOP_LIMIT; index++) {
            hologram.updateLine(index, topLines.get(index - 1));
        }
    }


    private static List<String> getTopsLines() {
        List<String> lines = new ArrayList<>();
        MongoCursor<Document> cursor = getTopDocuments();

        int currentTop = 1;

        // While documents existing in cursor get info and set to lines
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
            lines.add(ChatUtil.parse((Player) null, "<lang.lobby.parkour_top_score>", currentTop, getNameFromId(uuid), formattedTime + "s"));
            currentTop++;
        }

        // Fill until limit with blank tops
        while (currentTop != TOP_LIMIT) {
            lines.add(ChatUtil.parse((Player) null, "<lang.lobby.parkour_top_score>", currentTop, "-", "-"));
            currentTop++;
        }

        return lines;
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
