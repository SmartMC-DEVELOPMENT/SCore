package us.smartmc.moderation.staffmodebungee.instance;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.text.MessageFormat;

@Getter
public class StaffChatMessageRequest {

    public static final String MESSAGE_FORMAT = "&3[SC]&f {0} &8»&f {1}";

    public static final String CHANNEL_NAME = "staff-mode:chat_message";

    public static final String DATA_NAME_NAME = "name";
    public static final String DATA_MESSAGE_NAME = "msg";

    private final Document document;
    private final String name;
    private final String message;

    public StaffChatMessageRequest(String name, String message) {
        this.name = name;
        this.message = message;
        this.document = new Document().append(DATA_NAME_NAME, name).append(DATA_MESSAGE_NAME, message);
    }

    public void publish() {
        RedisConnection.mainConnection.getResource().publish(CHANNEL_NAME, document.toJson());
    }

    public String getFormattedMessage(ProxiedPlayer player) {
        return MessageFormat.format(ChatUtil.parse(player, MESSAGE_FORMAT), name, message);
    }

    public static StaffChatMessageRequest fromJson(String json) {
        Document document = Document.parse(json);
        return new StaffChatMessageRequest(document.getString(DATA_NAME_NAME),
                document.getString(DATA_MESSAGE_NAME));
    }

}
