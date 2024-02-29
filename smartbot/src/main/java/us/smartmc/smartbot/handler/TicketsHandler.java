package us.smartmc.smartbot.handler;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.connection.MongoDBConnection;

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
