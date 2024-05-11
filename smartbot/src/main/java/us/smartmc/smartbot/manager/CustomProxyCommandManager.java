package us.smartmc.smartbot.manager;

import com.mongodb.client.MongoCollection;

import lombok.Getter;
import org.bson.Document;
import us.smartmc.smartbot.connection.MongoDBConnection;
import us.smartmc.smartbot.connection.RedisConnection;

import java.util.*;
import java.util.regex.Pattern;

@Getter
public class CustomProxyCommandManager {

    private static final String CHANNEL_ID = "proxy@custom_cmds";

    private static final Set<Long> toDeleteMap = new HashSet<>();

    private final Timer timer = new Timer();

    public CustomProxyCommandManager() {

        loadQuotesDocuments().forEach(document -> {
            long expiration = Long.parseLong(document.getString("_id").replace("quote-", ""));
            System.out.println("Loaded quoute " + expiration);
            toDeleteMap.add(expiration);
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long currentTimeStamp = System.currentTimeMillis() / 1000;
                new HashSet<>(toDeleteMap).stream().filter(timestamp -> timestamp <= currentTimeStamp)
                        .forEach(CustomProxyCommandManager::remove);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    public static void register(long timestamp, String cmdId, String commandInstruction) {
        Document document = getQuery(timestamp).append(cmdId, List.of(commandInstruction));
        getCollection().insertOne(document);
        toDeleteMap.add(timestamp);
        registerAllowedCommand(cmdId);

        sendProxyCommandsReloadInstruction();
    }

    private static void unregisterAllowedCommands(Document document) {
        Document query = new Document("_id", "global");
        Document global = getAllowedCommandsCollection().find(query).first();
        if (global == null) return;
        List<String> list = global.getList("allowed", String.class);
        document.keySet().stream().filter(key -> !key.equals("_id")).forEach(list::remove);
        getAllowedCommandsCollection().deleteMany(query);
        getAllowedCommandsCollection().insertOne(global);
    }

    private static void registerAllowedCommand(String cmdId) {
        Document query = new Document("_id", "global");
        Document global = getAllowedCommandsCollection().find(query).first();
        if (global == null) return;
        List<String> list = global.getList("allowed", String.class);
        list.add(cmdId);
        getAllowedCommandsCollection().deleteMany(query);
        getAllowedCommandsCollection().insertOne(global);
    }

    public static void remove(long timestamp) {
        toDeleteMap.remove(timestamp);
        sendProxyRemoveCommandHandler(timestamp);
        Document document = getCollection().find(getQuery(timestamp)).first();
        if (document != null) unregisterAllowedCommands(document);
        getCollection().deleteMany(getQuery(timestamp));
    }

    public static void sendProxyCommandsReloadInstruction() {
        RedisConnection.mainConnection.send(CHANNEL_ID, "reload");
    }

    public static void sendProxyRemoveCommandHandler(long timestamp) {
        RedisConnection.mainConnection.send(CHANNEL_ID, "removeCommandHandler " + getId(timestamp));
        System.out.println("Removed quote of " + timestamp);
    }

    public Collection<Document> loadQuotesDocuments() {
        Set<Document> collection = new HashSet<>();
        Pattern regex = Pattern.compile("^quote-.*"); // Expresión regular para buscar _id que comienzan con "quote-"
        for (Document document : getCollection().find(new Document("_id", regex))) {
            collection.add(document);
        }
        return collection;
    }

    public static Document getQuery(long timestamp) {
        return new Document("_id", getId(timestamp));
    }

    public static String getId(long timestamp) {
        return "quote-" + timestamp;
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection
                .getDatabase("proxy_data")
                .getCollection("custom_proxy_commands");
    }

    private static MongoCollection<Document> getAllowedCommandsCollection() {
        return MongoDBConnection.mainConnection
                .getDatabase("proxy_data")
                .getCollection("allowed_commands");
    }

}
