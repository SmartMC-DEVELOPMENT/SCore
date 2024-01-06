package us.smartmc.snowgames.player;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.snowgames.manager.ItemCooldownManager;

@Getter
public class FFAPlayer extends GamePlayer {


    private static final MongoCollection<Document> collection = MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("snowgames_ffa");

    private Document statsDocument;

    private int killStreak;

    protected FFAPlayer(Player player) {
        super(player);
        statsDocument = collection.find(getQuery()).first();
        if (statsDocument == null) statsDocument = getQuery();

        killStreak = 0;
    }

    // Obtener / Returnar 0
    public int getIntStat(String name) {
        return statsDocument.getInteger(name, 0);
    }

    // Establecer valor
    public void setIntStat(String name, int value) {
        statsDocument.put(name, value);
    }

    // Sumar valor +1
    public void sumIntStat(String name) {
        int currentAmount = getIntStat(name);
        setIntStat(name, currentAmount + 1);
    }

    public void addKill() {
        killStreak++;
        setIntStat("max_kill_streak", Math.max(killStreak, getMaxStreak()));
        sumIntStat("kills");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10F, 10F);
    }

    public int getKills() {
        return getIntStat("kills");
    }

    public void addDeath() {
        sumIntStat("deaths");
        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1, 1);
        killStreak = 0;
        // Clear cooldown from items
        ItemCooldownManager.clear(player);
    }

    public int getDeaths() {
        return getIntStat("deaths");
    }

    public int getCurrentStreak() {
        return killStreak;
    }

    public int getMaxStreak() {
        return getIntStat("max_kill_streak");
    }

    @Override
    public void load() {

    }

    public void saveStats() {
        collection.deleteMany(getQuery());
        collection.insertOne(statsDocument);
    }

    private Document getQuery() {
        return new Document().append("_id", player.getUniqueId().toString());
    }
}
