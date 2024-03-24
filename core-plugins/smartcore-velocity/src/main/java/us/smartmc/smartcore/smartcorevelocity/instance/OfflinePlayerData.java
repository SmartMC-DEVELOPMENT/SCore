package us.smartmc.smartcore.smartcorevelocity.instance;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.manager.OfflinePlayerDataManager;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

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
        document = OfflinePlayerDataManager.getColletion().find(new Document().append("_id", uuid.toString())).first();

        if (document == null) {
            playedBefore = false;
            document = getQuery();
        }
        cache.put(uuid, this);

        if (!playedBefore) {
            save();
            Player player = VelocityPluginsAPI.proxy.getPlayer(uuid).get();
            if (player == null) return;
            player.disconnect(Component.text(ChatUtil.parse(player, "&bHello {0}!\n&cPlease reconnect, welcome to the network :D!", player.getUsername())));
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

    public void parse(Player player) {
        document.put("name", player.getUsername());
        document.put("lowercase_name", player.getUsername().toLowerCase());
        document.put("ip_address", player.getRemoteAddress().getAddress().toString().replace("/", ""));
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
        OfflinePlayerDataManager.getColletion().deleteMany(getQuery());

        // Delete documents with same name
        OfflinePlayerDataManager.getColletion().deleteMany(new Document("name", getName()));
        OfflinePlayerDataManager.getColletion().insertOne(document);
    }

    public void removeCache() {
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

    public static OfflinePlayerData get(Player player) {
        return cache.get(player.getUniqueId());
    }

    public static OfflinePlayerData get(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        return new OfflinePlayerData(uuid);
    }

    public static OfflinePlayerData get(String name) {
        name = name.toLowerCase();
        Document document = OfflinePlayerDataManager.getColletion().find(new Document().append("lowercase_name", name)).first();
        UUID uuid = UUID.fromString(document.getString("_id"));
        if (cache.containsKey(uuid)) return cache.get(uuid);
        return new OfflinePlayerData(uuid);
    }

}
