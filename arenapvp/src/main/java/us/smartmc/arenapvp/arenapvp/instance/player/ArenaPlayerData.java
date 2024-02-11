package us.smartmc.arenapvp.arenapvp.instance.player;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data.OfflineGamePlayerData;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data.OfflinePlayerDataInfo;

import java.util.UUID;

@OfflinePlayerDataInfo(database = "player_data", collection = "arenapvp")
public class ArenaPlayerData extends OfflineGamePlayerData {

    private int ranked_elo;

    @Getter
    @Setter
    private long
            kills, deaths,
            wins, loses,
            timePlayed,
            winStreak, bestWinStreak;

    public ArenaPlayerData(UUID uuid) {
        super(uuid);
    }

    @Override
    public void load() {
        setKills(getNumberStat("kills").longValue());
        setDeaths(getNumberStat("deaths").longValue());
        setWins(getNumberStat("wins").longValue());
        setLoses(getNumberStat("loses").longValue());
        setTimePlayed(getNumberStat("timePlayed").longValue());
        setWinStreak(getNumberStat("winStreak").longValue());
        setBestWinStreak(getNumberStat("bestWinStreak").longValue());
    }

    @Override
    public Document getQueryDocument() {
        return new Document("_id", getUUID().toString());
    }

    @Override
    public MongoDBConnection getMongoConnection() {
        return MongoDBConnection.mainConnection;
    }
}
