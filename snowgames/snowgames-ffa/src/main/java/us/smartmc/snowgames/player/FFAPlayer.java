package us.smartmc.snowgames.player;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.snowgames.manager.ItemCooldownManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FFAPlayer extends GamePlayer {


    private static final MongoCollection<Document> collection = MongoDBConnection.mainConnection.getDatabase("SnowMatch").getCollection("playerStats");
    public Map<UUID, Integer> currentStreak;
    public int streak;

    @Getter
    private Document statsDocument;

    protected FFAPlayer(Player player) {
        super(player);
        // Intenta buscar el documento de stats, si no encuentra se establece a el query que solo tiene el _id
        statsDocument = collection.find(getQuery()).first();
        if (statsDocument == null) statsDocument = getQuery();

        currentStreak = new HashMap<>();
        streak = 0;
    }

    // Escribe aqui las stats que quieres poner:: kills, deaths, racha, maxima racha
    // Y ahora puedes hacer todos los metodos o dejarlo asi, lo importante es cuando un player se sale llamar metodo saveStats

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
        sumIntStat("kills");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10F, 10F);
        addStreak();
    }

    public int getKills() {
        return getIntStat("kills");
    }

    public void addDeath() {
        sumIntStat("deaths");
        player.playSound(player.getLocation(), Sound.CAT_PURR, 10F, 10F);
        setMaxStreak();
        setStreakTo0();
        // Clear cooldown from items
        ItemCooldownManager.clear(player);
    }

    public int getDeaths() {
        return getIntStat("deaths");
    }

    public void addStreak() {
        streak = currentStreak.get(player.getUniqueId());
    }

    public void setStreakTo0() {
        currentStreak.remove(player.getUniqueId());
    }

    public int getCurrentStreak() {
        return streak;
    }

    public void setMaxStreak() {
        if (getMaxStreak() > streak) return;
        setIntStat("max_streak", streak);
    }

    public int getMaxStreak() {
        return getIntStat("max_streak");
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
