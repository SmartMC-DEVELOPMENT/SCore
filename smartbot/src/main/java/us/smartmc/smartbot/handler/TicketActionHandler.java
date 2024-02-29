package us.smartmc.smartbot.handler;

import lombok.Getter;
import org.bson.Document;
import us.smartmc.smartbot.connection.MongoDBConnection;

public class TicketActionHandler {

    @Getter
    private final String guildID, channelID, emoji, channelPrefix;

    @Getter
    private String roleMenction;

    private TicketActionHandler(Document document) {
        Document validation = document.get("validation", Document.class);
        guildID = validation.getString("guild_id");
        channelID = validation.getString("channel_id");
        emoji = validation.getString("emoji");
        channelPrefix = document.getString("channel_prefix");

        roleMenction = document.getString("role_mention");
    }

    public String getParsedNamePlaceholder(String... args) {
        StringBuilder stringBuilder = new StringBuilder(channelPrefix);
        for (String arg : args) {
            stringBuilder.append("-").append(arg);
        }
        return stringBuilder.toString();
    }

    public static TicketActionHandler get(String guildID, String channelID, String emoji) {
        Document query = new Document("validation", new Document().append("guild_id", guildID).append("channel_id", channelID).append("emoji", emoji));
        Document mongoDoc = MongoDBConnection.mainConnection.getDatabase("smartbot").getCollection("tickets_actions").find(query).first();
        if (mongoDoc == null) return null;
        return new TicketActionHandler(mongoDoc);
    }

}
