package me.imsergioh.smartcorewaterfall.instance;

import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.manager.OfflinePlayerDataManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class OfflinePlayerData {

    private static final HashMap<UUID, OfflinePlayerData> cache = new HashMap<>();

    private final UUID uuid;
    private boolean playedBefore = true;
    private Document document;

    public OfflinePlayerData(UUID uuid) {
        this.uuid = uuid;
        document = OfflinePlayerDataManager.getColletion().find(new Document().append("_id", uuid.toString())).first();

        if (document == null) {
            playedBefore = false;
            document = getQuery();
        }
        cache.put(uuid, this);

        if (!playedBefore) {
            save();
            ProxiedPlayer player = SmartCoreWaterfall.getPlugin().getProxy().getPlayer(uuid);
            if (player == null) return;
            player.disconnect(ChatUtil.parse(player, "&bHello {0}!\n&cPlease reconnect, welcome to the network :D!", player.getName()));
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                fetchLanguage();
            }
        }, 250);
    }

    private void fetchLanguage() {
        Document document = MongoDBConnection.mainConnection.getDatabase("player_data").getCollection("core_players").find(
                new Document("_id", uuid.toString())
        ).first();
        Language language = Language.getDefault();
        if (document != null) language = Language.valueOf(document.getString("lang"));
        PlayerLanguages.register(uuid, language);
    }

    public void parse(ProxiedPlayer player) {
        document.put("name", player.getName());
        document.put("lowercase_name", player.getName().toLowerCase());
        document.put("ip_address", player.getPendingConnection().getAddress().getAddress().toString().replace("/", ""));
        save();
    }

    public String getIP() {
        return document.getString("ip_address");
    }

    public String getName() {
        return document.getString("name");
    }

    public void save() {
        OfflinePlayerDataManager.getColletion().deleteMany(getQuery());
        OfflinePlayerDataManager.getColletion().insertOne(document);
    }

    public void removeCache() {
        cache.remove(uuid);
    }

    public Document getDocument() {
        return document;
    }

    public Document getQuery() {
        return new Document().append("_id", uuid.toString());
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean hasPlayedBefore() {
        return playedBefore;
    }

    public static OfflinePlayerData get(ProxiedPlayer player) {
        return cache.get(player.getUniqueId());
    }

    public static OfflinePlayerData get(UUID playerUuid) {
        return cache.get(playerUuid);
    }

    public static OfflinePlayerData get(String name) {
        name = name.toLowerCase();
        Document document = OfflinePlayerDataManager.getColletion().find(new Document().append("lowercase_name", name)).first();
        UUID uuid = UUID.fromString(document.getString("_id"));
        if (cache.containsKey(uuid)) return cache.get(uuid);
        return new OfflinePlayerData(uuid);
    }

}
