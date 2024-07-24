package us.smartmc.smartcore.proxy.instance.sanction;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import us.smartmc.smartcore.proxy.instance.OfflinePlayerData;
import us.smartmc.smartcore.proxy.messages.SanctionsManagerMessages;
import us.smartmc.smartcore.proxy.util.TimeUtils;

import java.util.Random;
import java.util.UUID;

public class PlayerSanction extends MongoDBPluginConfig {

    private static final String DATABASE = "proxy_data";
    private static final String COLLECTION = "player_sanctions";

    @Getter
    private final String sanctionId;
    private final UUID playerId;

    // Create offline
    public PlayerSanction(UUID uuid, UUID creatorId, SanctionType type, TimeUtils timeUtils, String reason) {
        super(DATABASE, COLLECTION, new Document("_id", UUID.randomUUID().toString()));
        this.playerId = uuid;
        this.sanctionId = getNewSanctionId();
        long createdAt = System.currentTimeMillis() / 1000;
        put("player_id", uuid.toString());
        put("created_by", creatorId.toString());

        ProxiedPlayer player = BungeeCordPluginsAPI.proxy.getPlayer(creatorId);
        if (player != null) {
            put("created_by_name", player.getName());
        } else {
            put("created_by_name", "Console");
        }

        put("created_at", createdAt);
        put("sanction_id", sanctionId);

        OfflinePlayerData playerData = OfflinePlayerData.get(uuid);

        put("ip_address", playerData.getIP());
        if (timeUtils != null) {
            put("expiration_at", timeUtils.addToTimestamp(createdAt));
        }
        put("type", type.name());
        if (reason != null || !reason.isEmpty()) put("reason", reason);

        save();
        checkDisconnectPlayer(true);
        sendPlayerInfo(true);

        OfflinePlayerData.removeCache(playerId);
    }

    // Load
    public PlayerSanction(UUID uuid, Document document) {
        super(DATABASE, COLLECTION, document);
        this.playerId = uuid;
        load();
        this.sanctionId = getString("sanction_id");
        save();
        checkDisconnectPlayer(false);
        sendPlayerInfo(false);
        System.out.println("Loaded sanction " + sanctionId + " isActive " + isActive() + " expiration " + getExpiration());
    }

    public void sendPlayerInfo(boolean created) {
        if (!(getType().equals(SanctionType.MUTE) || getType().equals(SanctionType.WARN))) return;

        ProxiedPlayer player = BungeeCordPluginsAPI.getPlugin().getProxy().getPlayer(playerId);

        if (getType().equals(SanctionType.MUTE)) {
            if (created) {
                String message = LanguagesHandler.get(Language.getDefault()).get(SanctionsManagerMessages.NAME)
                        .getString("message_been_muted");
                player.sendMessage(ChatUtil.parse(player, message, getReason(), getExpiration(), getSanctionId()));
            } else {
                if (!isActive()) return;
                String message = LanguagesHandler.get(Language.getDefault()).get(SanctionsManagerMessages.NAME)
                        .getString("message_muted");
                player.sendMessage(ChatUtil.parse(player, message, getReason(), getExpiration(), getSanctionId()));
            }
        }

        if (getType().equals(SanctionType.WARN)) {
            if (created) {
                String message = LanguagesHandler.get(Language.getDefault()).get(SanctionsManagerMessages.NAME)
                        .getString("message_been_warned");
                player.sendMessage(ChatUtil.parse(player, message, getReason(), getSanctionId()));
            }
        }
    }

    public boolean isActive() {
        if (!hasExpiration()) return true;
        long expireAt = getLong("expiration_at");
        long currentTimeStamp = System.currentTimeMillis() / 1000;
        return expireAt > currentTimeStamp;
    }

    private void checkDisconnectPlayer(boolean created) {
        if (!created && getType().equals(SanctionType.KICK)) return;
        if (hasExpiration()) {
            if (!isActive()) return;
        }

        if (!(getType().equals(SanctionType.BAN) || getType().equals(SanctionType.KICK))) return;

        BaseComponent message = null;

        ProxiedPlayer player = BungeeCordPluginsAPI.proxy.getPlayer(playerId);

        if (player == null) return;

        if (getType().equals(SanctionType.KICK)) {
            message = ChatUtil.parse(player, "<lang.sanctions_manager.kick_message>", getReason(), getSanctionId());
        }

        if (getType().equals(SanctionType.BAN)) {
            message = ChatUtil.parse(player, "<lang.sanctions_manager.ban_message>", getReason(), getExpiration(), getSanctionId());
        }
        if (message == null) message = new TextComponent();
        player.disconnect(message);
    }

    public String getExpiration() {
        if (hasExpiration()) {
            return TimeUtils.toString(getLong("expiration_at"));
        } else {
            Language language = Language.getDefault();
            return SanctionsManagerMessages.get(language, "no_expiration");
        }
    }

    public boolean hasExpiration() {
        return containsKey("expiration_at");
    }

    public String getReason() {
        if (containsKey("reason")) {
            return getString("reason");
        }
        return SanctionsManagerMessages.get(Language.getDefault(),
                "noReason");
    }

    public SanctionType getType() {
        return SanctionType.valueOf(getString("type"));
    }

    public String getCreatedByName() {
        return getString("created_by_name");
    }

    public UUID getCreatedById() {
        return UUID.fromString(getString("created_by"));
    }

    public UUID getUUID() {
        return playerId;
    }

    public static MongoCollection<Document> sanctionsCollection() {
        return MongoDBConnection.mainConnection.getDatabase(DATABASE).getCollection(COLLECTION);
    }

    private static String getNewSanctionId() {
        String format = "xxxxxx";
        while (format.contains("x")) {
            format = format.replaceFirst("x", String.valueOf(new Random().nextInt(9)));
        }
        return format;
    }

}
