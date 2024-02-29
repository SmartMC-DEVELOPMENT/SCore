
package us.smartmc.smartbot.instance.ticket;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

public class TicketStorageSaver {

    @Getter
    private final Document identifierDoc;
    private final TextChannel textChannel;

    public TicketStorageSaver(Document identifierDoc, TextChannel textChannel) {
        this.identifierDoc = identifierDoc;
        this.textChannel = textChannel;
        System.out.println("TicketSaver idDoc -> " + identifierDoc);
        forEachMessage().forEach(message -> {
            System.out.println("TicketSaver -> " + message);
        });
    }

    private Set<Message> forEachMessage() {
        Set<Message> messages = new HashSet<>();
        for (Message message : textChannel.getHistory().retrievePast(10).complete()) {
            if (message == null) break;
            messages.add(message);
        }
        return messages;
    }
}
