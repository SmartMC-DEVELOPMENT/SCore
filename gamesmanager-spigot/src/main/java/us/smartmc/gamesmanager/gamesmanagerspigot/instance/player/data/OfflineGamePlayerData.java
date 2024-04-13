package us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

public abstract class OfflineGamePlayerData implements IOfflineGamePlayerData {

    private final UUID uuid;

    @Getter
    private Document document;
    private final String database, collection;

    public OfflineGamePlayerData(UUID uuid) {
        this.uuid = uuid;
        OfflinePlayerDataInfo info = getClass().getDeclaredAnnotation(OfflinePlayerDataInfo.class);
        this.database = info.database();
        this.collection = info.collection();
        document = getCollection().find(getQueryDocument()).first();
        if (document == null) document = getQueryDocument();
    }

    @Override
    public void save() {
        getCollection().deleteMany(getQueryDocument());
        getCollection().insertOne(getDocument());
    }

    @Override
    public void register(String key, Object value) {
        if(getDocument().containsKey(key)) return;
        set(key, value);
    }

    @Override
    public void set(String key, Object value) {
        getDocument().put(key, value);
    }

    @Override
    public void addNumberStat(String key) {
        long number = getNumberStat(key).longValue();
        number++;
        getDocument().put(key, number);
    }

    @Override
    public void subtractNumberStat(String key) {
        long number = getNumberStat(key).longValue();
        number--;
        getDocument().put(key, number);
    }

    @Override
    public void resetNumberStat(String key) {
        getDocument().put(key, 0);
    }

    @Override
    public Number getNumberStat(String key) {
        if (!getDocument().containsKey(key)) return 0;
        return getDocument().get(key, Number.class);
    }

    @Override
    public Object get(String key) {
        return getDocument().get(key);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public MongoDatabase getDatabase() {
        return getMongoConnection().getDatabase(database);
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return getDatabase().getCollection(collection);
    }

}
