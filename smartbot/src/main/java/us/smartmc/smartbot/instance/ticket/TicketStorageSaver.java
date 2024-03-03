
package us.smartmc.smartbot.instance.ticket;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bson.Document;
import us.smartmc.smartbot.connection.MongoDBConnection;
import us.smartmc.smartbot.handler.TicketsHandler;

import java.time.OffsetDateTime;
import java.util.*;

public class TicketStorageSaver {

    private static final Map<String, TicketStorageSaver> savers = new HashMap<>();

    @Getter
    private final Message initialMessage;
    private final Document identifierDoc;
    @Getter
    private final String guildId, channelId;
    private final TextChannel textChannel;

    private final List<String> registry = new ArrayList<>();

    private TicketStorageSaver(Message message, Document identifierDoc, String guildId, TextChannel textChannel) {
        savers.put(textChannel.getId(), this);
        this.initialMessage = message;
        this.identifierDoc = identifierDoc;
        System.out.println("Created TicketStorageSaver -> " + identifierDoc);
        this.guildId = guildId;
        this.channelId = textChannel.getId();
        this.textChannel = textChannel;
        forEachMessage().forEach(this::registerMessage);
    }

    public void save() {
        System.out.println("Start save ticket " + channelId);
        savers.remove(channelId);

        StringBuilder transcriptBuilder = new StringBuilder();
        for (String messageLine : registry) {
            transcriptBuilder.append(messageLine).append("\n");
        }

        Document doc = new Document()
                .append("_id", channelId)
                .append("guild_id", guildId)
                .append("identifier", identifierDoc)
                .append("transcript", transcriptBuilder.toString());
        getCollection().insertOne(doc);
        try {
            textChannel.delete().complete();
        } catch (Exception e) {
            if (e.getMessage().contains("Unknown Channel")) return;
            throw new RuntimeException(e);
        }
    }

    public void registerMessage(Message message) {
        String formattedMessage = getMessageRegistry(message);
        if (registry.contains(formattedMessage)) return;
        registry.add(formattedMessage);
    }

    private String getMessageRegistry(Message message) {
        OffsetDateTime time = message.getTimeCreated();
        String formattedDate = "[" + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "]";
        String formattedAuthor = message.getAuthor().getName();

        return formattedDate + " " + formattedAuthor + ": " + message.getContentRaw();
    }

    private Collection<Message> forEachMessage() {
        List<Message> list = new ArrayList<>();
        List<Message> messages = new ArrayList<>(MessageHistory.getHistoryFromBeginning(textChannel).complete().getRetrievedHistory());
        Collections.reverse(messages);
        for (Message message : messages) {
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

    public static void registerTicketSaver(Message message, TextChannel channel) {
        if (savers.containsKey(channel.getId())) return;
        new TicketStorageSaver(message, TicketsHandler.getTicketIdentifier(channel.getId()), channel.getGuild().getId(), channel);
    }

    public static TicketStorageSaver getByChannelId(TextChannel channel) {
        String id = channel.getId();
        if (!savers.containsKey(id)) {
            List<Message> list = new ArrayList<>(channel.getHistoryFromBeginning(4).complete().getRetrievedHistory());
            Collections.reverse(list);
            registerTicketSaver(list.get(0), channel);
        }
        return savers.get(id);
    }

}
