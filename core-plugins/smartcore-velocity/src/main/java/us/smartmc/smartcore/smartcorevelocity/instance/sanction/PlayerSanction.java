package us.smartmc.smartcore.smartcorevelocity.instance.sanction;

import com.mongodb.client.MongoCollection;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.messages.SanctionsManagerMessages;
import us.smartmc.smartcore.smartcorevelocity.util.TimeUtils;
import us.smartmc.smartcore.velocitycore.manager.VelocityPluginsAPI;

import java.util.UUID;

public class PlayerSanction extends MongoDBPluginConfig {

    private static final String DATABASE = "proxy_data";
    private static final String COLLECTION = "player_sanctions";

    private final Player player;
    private final UUID uuid;

    // Create
    public PlayerSanction(UUID uuid, SanctionType type, TimeUtils timeUtils, String reason) {
        super("proxy_data", "player_sanctions", new Document("_id", UUID.randomUUID().toString()));
        this.uuid = uuid;
        player = null;
        long createdAt = System.currentTimeMillis() / 1000;
        put("player_id", uuid.toString());
        put("created_at", createdAt);
        Player player = VelocityPluginsAPI.proxy.getPlayer(uuid).get();
        put("ip_address", player.getRemoteAddress().getAddress().toString());
        if (timeUtils != null) {
            put("expiration_at", timeUtils.addToTimestamp(createdAt));
        }
        put("type", type.name());
        if (reason != null || reason.length() >= 1) put("reason", reason);

        save();
        checkDisconnectPlayer(true);
        sendPlayerInfo(true);
    }

    // Load
    public PlayerSanction(Player player, Document document) {
        super("proxy_data", "player_sanctions", document);
        this.player = player;
        load();
        this.uuid = UUID.fromString(document.getString("player_id"));
        save();
        checkDisconnectPlayer(false);
        sendPlayerInfo(false);
    }

    public void sendPlayerInfo(boolean created) {
        if (!(getType().equals(SanctionType.MUTE) || getType().equals(SanctionType.WARN))) return;

        if (getType().equals(SanctionType.MUTE)) {
            if (created) {
                String message = LanguagesHandler.get(Language.getDefault()).get(SanctionsManagerMessages.NAME)
                        .getString("message_been_muted");
                player.sendMessage(Component.text(ChatUtil.parse(player, message, getReason(), getExpiration())));
            } else {
                if (!isActive()) return;
                String message = LanguagesHandler.get(Language.getDefault()).get(SanctionsManagerMessages.NAME)
                        .getString("message_muted");
                player.sendMessage(Component.text(ChatUtil.parse(player, message, getReason(), getExpiration())));
            }
        }

        if (getType().equals(SanctionType.WARN)) {
            if (created) {
                String message = LanguagesHandler.get(Language.getDefault()).get(SanctionsManagerMessages.NAME)
                        .getString("message_been_warned");
                player.sendMessage(Component.text(ChatUtil.parse(player, message, getReason())));
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
        Player player = VelocityPluginsAPI.proxy.getPlayer(uuid).get();

        if (!created && getType().equals(SanctionType.KICK)) return;


        if (hasExpiration()) {
            if (!isActive()) return;
        }

        if (!(getType().equals(SanctionType.BAN) || getType().equals(SanctionType.KICK))) return;
        String message = null;

        if (getType().equals(SanctionType.KICK)) {
            Language language = Language.getDefault();
            message = ChatUtil.parse(LanguagesHandler.get(language)
                    .get(SanctionsManagerMessages.NAME).getString("kick_message"), getReason());
        }

        if (getType().equals(SanctionType.BAN)) {
            Language language = Language.getDefault();
            message = ChatUtil.parse(player, LanguagesHandler.get(language)
                    .get(SanctionsManagerMessages.NAME).getString("ban_message"), getReason(), getExpiration());
        }
        if (message == null) message = "";
        player.disconnect(Component.text(message));
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
                "no_reason");
    }

    public SanctionType getType() {
        return SanctionType.valueOf(getString("type"));
    }

    public UUID getUUID() {
        return uuid;
    }

    public static MongoCollection<Document> sanctionsCollection() {
        return MongoDBConnection.mainConnection.getDatabase(DATABASE).getCollection(COLLECTION);
    }
}
