
package us.smartmc.smartbot.instance.ticket;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bson.Document;
import us.smartmc.smartbot.connection.MongoDBConnection;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TicketStorageSaver {

    private static final Map<String, TicketStorageSaver> savers = new HashMap<>();

    private final Document identifierDoc;
    private final TextChannel textChannel;

    private final Set<Document> registry = new HashSet<>();

    public TicketStorageSaver(Document identifierDoc, String channelId, TextChannel textChannel) {
        savers.put(channelId, this);
        this.identifierDoc = identifierDoc;
        this.textChannel = textChannel;
        System.out.println("TicketSaver idDoc -> " + identifierDoc);
        forEachMessage().forEach(message -> {
            System.out.println("TicketSaver -> " + message);
        });
    }

    public void save() {
        Document doc = new Document()
                .append("_id", textChannel.getId())
                .append("identifier", identifierDoc)
                .append("messages_registry", registry);
        getCollection().insertOne(doc);
        textChannel.delete().queue();
    }

    public void registerMessage(Message... messages) {
        for (Message message : messages) {
            registry.add(getMessageRegistry(message));
        }
    }

    private Document getMessageRegistry(Message message) {
        Document doc = new Document("_id", message.getId());
        doc.append("author_id", message.getAuthor().getId());
        doc.append("content_raw", message.getContentRaw());
        return doc;
    }

    private Set<Message> forEachMessage() {
        Set<Message> messages = new HashSet<>();
        for (Message message : textChannel.getHistory().retrievePast(10).complete()) {
            if (message == null) continue;
            messages.add(message);
        }
        return messages;
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase("smartbot")
                .getCollection("tickets_registry");
    }

    public static TicketStorageSaver getByChannelId(String id) {
        return savers.get(id);
    }

}
