package us.smartmc.smartbot.manager;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.bson.Document;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.connection.MongoDBConnection;

import java.util.HashMap;
import java.util.Objects;

public class AutoRoleManager {

    // KEY = MESSAGE ID - VALUE = GUILD ROLE ID
    private static final HashMap<String, String> data = new HashMap<>();

    public static void loadAutoRolesFromGuild(String guildId) {
        for (Document document : getCollection().find(new Document("guild", guildId))) {
            String messageId = document.getString("_id");
            String roleId = document.getString("role");
            data.put(messageId, roleId);
            System.out.println("Registered autorole " + roleId);
        }
    }

    public static void proveAdd(MessageReactionAddEvent event) {
        String roleId = data.get(event.getMessageId());
        if (roleId == null) return;
        Role role = event.getGuild().getRoleById(roleId);

        if (role == null) return;
        //event.getGuild().addRoleToMember(event.getMember()), role).queue();
        event.getMember().getGuild().addRoleToMember(event.getMember(), role).complete();
    }

    public static void proveRemove(MessageReactionRemoveEvent event) {
        if (!data.containsKey(event.getMessageId())) return;
        String roleId = data.get(event.getMessageId());
        if (roleId == null) return;
        Role role = event.getGuild().getRoleById(roleId);
        if (role == null) return;
        event.getMember().getGuild().removeRoleFromMember(event.getMember(), role).complete();
    }

    private static MongoCollection<Document> getCollection() {
        return MongoDBConnection.mainConnection
                .getDatabase("smartbot")
                .getCollection("autoroles");
    }

}
