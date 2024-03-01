
package us.smartmc.smartbot.instance.ticket;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bson.Document;
import us.smartmc.smartbot.connection.MongoDBConnection;

import java.util.*;

public class TicketStorageSaver {

    private static final Map<String, TicketStorageSaver> savers = new HashMap<>();

    private final Document identifierDoc;
    @Getter
    private final String guildId, channelId;
    private final TextChannel textChannel;

    private final LinkedHashSet<Document> registry = new LinkedHashSet<>();

    public TicketStorageSaver(Document identifierDoc, String guildId, TextChannel textChannel) {
        savers.put(textChannel.getId(), this);
        this.identifierDoc = identifierDoc;
        this.guildId = guildId;
        this.channelId = textChannel.getId();
        this.textChannel = textChannel;
        forEachMessage().forEach(message -> {
            registry.add(getMessageRegistry(message));
        });
    }

    public void save() {
        System.out.println("Start save ticket " + channelId);
        savers.remove(channelId);
        Document doc = new Document()
                .append("_id", channelId)
                .append("guild_id", guildId)
                .append("identifier", identifierDoc)
                .append("messages_registry", registry);
        getCollection().insertOne(doc);
        textChannel.delete().queue();
        System.out.println("End saved ticket " + channelId + "!");
    }

    public void registerMessage(Message message) {
        registry.add(getMessageRegistry(message));
    }

    private Document getMessageRegistry(Message message) {
        Document doc = new Document("_id", message.getId());
        doc.append("author_id", message.getAuthor().getId());
        String contentRaw = message.getContentRaw();
        if (!contentRaw.isEmpty())
            doc.append("content_raw", contentRaw);
        message.getAttachments().forEach(attachment -> {
            doc.append("attachment_" + attachment.getId(), attachment.toString());
        });
        message.getReactions().forEach(reaction -> {
            doc.append("reaction_" + reaction.getEmoji(), reaction.toString());
        });
        return doc;
    }

    private Collection<Message> forEachMessage() {
        List<Message> list = new ArrayList<>();
        for (Message message : MessageHistory.getHistoryFromBeginning(textChannel).complete().getRetrievedHistory()) {
            if (message == null) {
                break;
            }
            list.add(message);
        }
        return list;
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection.getDatabase("smartbot")
                .getCollection("tickets_registry");
    }

    public static TicketStorageSaver getByChannelId(Guild guild, TextChannel channel) {
        String id = channel.getId();
        if (!savers.containsKey(id)) {
            return new TicketStorageSaver(null, guild.getId(), channel);
        }
        return savers.get(id);
    }

}
