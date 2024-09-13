package us.smartmc.core.instance.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.event.PlayerUnloadEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.core.SmartCore;
import us.smartmc.core.backend.service.PlayersService;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class SmartCorePlayer extends CorePlayer {

    private static final SmartCore plugin = SmartCore.getPlugin();

    private static final HashMap<UUID, SmartCorePlayer> players = new HashMap<>();

    @Getter
    private PlayerCurrenciesHandler currenciesHandler;

    protected SmartCorePlayer(UUID uuid) {
        super(uuid);
        currenciesHandler = new PlayerCurrenciesHandler(this);

        // Backend context add
        ServicesManager.performWhenLoaded(PlayersService.class, playersService -> {
           playersService.registerPlayerContext(getBukkitPlayer());
        });
    }

    public void sendActionBar(String message) {
        // Convert the Player to a CraftPlayer to access NMS methods
        CraftPlayer craftPlayer = (CraftPlayer) bukkitPlayer;

        // Create the chat component with the message
        IChatBaseComponent chatComponent = new ChatComponentText(ChatUtil.parse(bukkitPlayer, message));

        // Create the packet to send the chat message as an action bar (parameter 2 = 2 for action bar)
        Packet<?> packet = new PacketPlayOutChat(chatComponent, (byte) 2);

        // Send the packet to the player
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void unload() {
        // Backend context remove
        ServicesManager.performWhenLoaded(PlayersService.class, playersService -> {
            playersService.unregisterPlayerContext(getBukkitPlayer());
        });

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
        SmartCorePlayer corePlayer = players.get(uuid);
        players.remove(uuid);
        if (corePlayer != null) {
            corePlayer.unload();
        }
    }

    public static void unload(Player player) {
        unload(player.getUniqueId());
    }

}
