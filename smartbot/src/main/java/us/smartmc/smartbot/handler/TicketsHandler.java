package us.smartmc.smartbot.handler;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.bson.Document;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.connection.MongoDBConnection;
import us.smartmc.smartbot.connection.RedisConnection;
import us.smartmc.smartbot.instance.ticket.TicketStorageSaver;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TicketsHandler {

    private static final Map<String, TicketsHandler> handlers = new HashMap<>();

    private final String guildID, sectionID;

    private TicketsHandler(String guildID, String ticketSection) {
        this.guildID = guildID;
        this.sectionID = ticketSection;
        if (sectionID != null) handlers.put(guildID, this);
    }

    public static Document getTicketIdentifier(String id) {
        String json = RedisConnection.mainConnection.getResource().get("discord_ticket." + id);
        if (json == null) return null;
        return Document.parse(json);
    }

    public static boolean isActiveUserTicketDelay(String id) {
        return RedisConnection.mainConnection.getResource().exists("discord_ticket_user_delay." + id);
    }

    public static boolean isActiveTicket(String id) {
        return RedisConnection.mainConnection.getResource().exists("discord_ticket." + id);
    }

    public static void registerUserTicketDelay(User user) {
        String key = "discord_ticket_user_delay." + user.getId();
        RedisConnection.mainConnection.getResource().set(key, String.valueOf(System.currentTimeMillis() / 1000));
        RedisConnection.mainConnection.getResource().expire(key, 60);
    }

    public static void registerTicket(String ticketID, String ticketChannelId, MessageReactionAddEvent event) {
        if (!(event.getChannel() instanceof TextChannel textChannel)) return;
        Document document = new Document("_id", ticketID).append("created_by", event.getUserId()).append("creted_at", String.valueOf(System.currentTimeMillis() / 1000));
        RedisConnection.mainConnection.getResource()
                .set("discord_ticket." + ticketChannelId, document.toJson());
    }

    public static void removeCacheTicket(String channelID) {
        RedisConnection.mainConnection.getResource().del("discord_ticket." + channelID);
    }

    public Guild getGuild() {
        return SmartBotMain.getJDA().getGuildById(guildID);
    }

    public static TicketsHandler loadTicketsHandler(String id) {
        if (handlers.containsKey(id)) return handlers.get(id);
        Document handler = getGuildHandlerDoc(id);
        if (handler == null) {
            System.out.println("Failed to -> loadRulesAndActions (" + id + ")");
            return null;
        }
        return new TicketsHandler(id, handler.getString("ticket_section"));
    }

    private static Document getGuildHandlerDoc(String id) {
        return getTicketActionsCollection().find(new Document("_id", "GuildHandler_" + id)).first();
    }

    private static MongoCollection<Document> getTicketRegistryCollection() {
        return MongoDBConnection.mainConnection.getDatabase("smartbot").getCollection("tickets_actions");
    }

    private static MongoCollection<Document> getTicketActionsCollection() {
        return MongoDBConnection.mainConnection.getDatabase("smartbot").getCollection("tickets_actions");
    }

}
