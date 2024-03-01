package us.smartmc.core.instance.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.event.PlayerUnloadEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.core.messages.GeneralMessages;
import us.smartmc.core.regions.controller.PlayerRegionSubscriber;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class SmartCorePlayer extends CorePlayer {

    private static final SmartCore plugin = SmartCore.getPlugin();

    public static final String COINS_PATH = "coins";
    private static final HashMap<UUID, SmartCorePlayer> players = new HashMap<>();
    private final PlayerRegionSubscriber playerRegionSubscriber;

    protected SmartCorePlayer(UUID uuid) {
        super(uuid);
        playerRegionSubscriber = new PlayerRegionSubscriber(this);
    }

    @Override
    public void unload() {
        Bukkit.getPluginManager().callEvent(new PlayerUnloadEvent(this));
        super.unload();
    }

    public void executeBungeeCordCommand(String command) {
        if (command.startsWith("/")) command = command.replaceFirst("/", "");

        RedisConnection.mainConnection.getResource()
                .publish("PlayerChat",
                        bukkitPlayer.getName() + "\n"
                        + command);
    }

    public void removeCoins(long amount, String reason) throws CorePluginException {
        long coins = getCoins();
        long withCoinsRemoved = coins - amount;

        if (withCoinsRemoved >= 0) {
            setCoins(withCoinsRemoved);
            String msgPath = amount == 1 ? "coin_removed" : "coins_removed";
            if (reason != null) {
                reason = "(" + reason + ")";
            } else {
                reason = "";
            }
            sendLanguageMessage(GeneralMessages.NAME, msgPath, amount, reason);

        } else {
            throw new CorePluginException("removeCoins method tried to remove more coins than player have!");
        }
        playSound(Sound.DIG_GRASS, 10, 10);
    }

    public void addCoins(long amount, String reason) {
        setCoins(getCoins() + amount);
        String msgPath = amount == 1 ? "coin_added" : "coins_added";

        if (reason != null) {
            reason = "(" + reason + ")";
        } else {
            reason = "";
        }
        sendLanguageMessage(GeneralMessages.NAME, msgPath, amount, reason);
        playSound(Sound.LEVEL_UP, 10, 10);
    }

    public long getCoins() {
        try {
            return get(COINS_PATH, Number.class).longValue();
        } catch (Exception ignore) {
        }
        return 0;
    }

    public void setCoins(long amount) {
        playerData.getDocument().put(COINS_PATH, amount);
    }

    public <T> T get(String path, Class<T> clazz) {
        return playerData.getDocument().get(path, clazz);
    }

    public void playSound(Sound sound, float v, float p) {
        if (!isOnline) return;
        try {
            bukkitPlayer.playSound(bukkitPlayer.getLocation(), sound, v, p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Player player() {
        return bukkitPlayer;
    }

    public UUID getUUID() {
        return uuid;
    }

    public static void register(Player player) {
        if (players.containsKey(player.getUniqueId())) return;
        players.put(player.getUniqueId(), new SmartCorePlayer(player.getUniqueId()));
    }

    public static SmartCorePlayer get(String name) {
        name = name.toLowerCase();

        final MongoDatabase database = MongoDBConnection.mainConnection.getDatabase("player_data");
        final MongoCollection<Document> mongoCollection = database.getCollection("offline_player_data");

        final Document document = mongoCollection.find(new Document().append("lowercase_name", name)).first();
        if (document == null) return null;
        final UUID uuid = UUID.fromString(document.getString("_id"));
        SmartCorePlayer corePlayer = get(uuid);
        return corePlayer == null ? new SmartCorePlayer(uuid) : corePlayer;
    }

    public static SmartCorePlayer get(UUID uuid) {
        return players.get(uuid);
    }

    public static SmartCorePlayer get(Player player) {
        return get(player.getUniqueId());
    }

    public static void unload(UUID uuid) {
        players.remove(uuid);
        SmartCorePlayer corePlayer = get(uuid);
        if (corePlayer != null) corePlayer.unload();
        players.remove(uuid);
    }

    public static void unload(Player player) {
        unload(player.getUniqueId());
    }

}
