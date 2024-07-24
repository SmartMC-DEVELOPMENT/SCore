package us.smartmc.smartcore.proxy.instance;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import us.smartmc.smartcore.proxy.manager.OfflinePlayerDataManager;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class OfflinePlayerData {

    private static final HashMap<UUID, OfflinePlayerData> cache = new HashMap<>();

    private final UUID uuid;
    private boolean playedBefore = true;
    @Getter
    private Document document;

    public OfflinePlayerData(UUID uuid) {
        this.uuid = uuid;
        document = OfflinePlayerDataManager.getCollection().find(getQuery()).first();

        if (document == null) {
            playedBefore = false;
            document = getQuery();
        }
        cache.put(uuid, this);

        if (!playedBefore) {
            save();
            ProxiedPlayer player = BungeeCordPluginsAPI.proxy.getPlayer(uuid);
            player.disconnect(ChatUtil.parse(player, "&b¡Hola {0}!\n&cPor favor reconectate, bienvenido a la network :D!", player.getName()));
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
        String ipAddress = player.getPendingConnection().getAddress().toString().replace("/", "");
        if (ipAddress.contains(":")) ipAddress = ipAddress.split(":")[0];
        document.put("ip_address", ipAddress);
        document.put("last_update", System.currentTimeMillis() / 1000);
        save();
    }

    public String getIP() {
        return document.getString("ip_address");
    }

    public String getName() {
        return document.getString("name");
    }

    public void save() {
        OfflinePlayerDataManager.getCollection().deleteMany(getQuery());

        // Delete documents with same name
        OfflinePlayerDataManager.getCollection().deleteMany(new Document("name", getName()));
        OfflinePlayerDataManager.getCollection().deleteMany(new Document("_id", uuid.toString()));
        OfflinePlayerDataManager.getCollection().insertOne(document);
    }

    public static void removeCache(UUID uuid) {
        cache.remove(uuid);
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

    public static OfflinePlayerData get(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        return new OfflinePlayerData(uuid);
    }

    public static OfflinePlayerData get(String name) {
        name = name.toLowerCase();
        Document document = OfflinePlayerDataManager.getCollection().find(new Document().append("lowercase_name", name)).first();
        UUID uuid = UUID.fromString(document.getString("_id"));
        if (cache.containsKey(uuid)) return cache.get(uuid);
        return new OfflinePlayerData(uuid);
    }

}
