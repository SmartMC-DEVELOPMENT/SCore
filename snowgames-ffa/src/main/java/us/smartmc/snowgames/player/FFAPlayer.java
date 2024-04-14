package us.smartmc.snowgames.player;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data.IOfflineGamePlayerData;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;
import us.smartmc.snowgames.manager.ItemCooldownManager;

import java.util.UUID;

@Getter
public class FFAPlayer extends GamePlayer {

    protected final Player player;
    private Document statsDocument;

    private int killStreak;

    public FFAPlayer(GamePlayerManager<?> manager, Player player) {
        super(manager, player.getUniqueId());
        this.player = player;
    }

    private void loadStats() {
        statsDocument = getCollection().find(getQuery()).first();
        if (statsDocument == null) statsDocument = getQuery();
        statsDocument.put("name", player.getName());
    }

    // Obtener / Returnar 0
    public int getIntStat(String name) {
        if (statsDocument == null) return 0;
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
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10F, 10F);
    }

    public int getKills() {
        return getIntStat("kills");
    }

    public void addDeath() {
        sumIntStat("deaths");
        //player.playSound(player.getLocation(), Sound.CAT_MEOW, 1, 1);
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

    public void saveStats() {
        getCollection().deleteMany(getQuery());
        getCollection().insertOne(statsDocument);
    }

    private Document getQuery() {
        return new Document().append("_id", player.getUniqueId().toString());
    }

    @Override
    public void load() {
        loadStats();
        killStreak = 0;
    }

    @Override
    public void unload() {
        saveStats();
    }

    @Override
    public IOfflineGamePlayerData getData() {
        return null;
    }

    public static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("snowgames_ffa");
    }
}
