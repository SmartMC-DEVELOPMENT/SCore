package us.smartmc.smartbot.instance.minecraft;

import com.mongodb.client.MongoCollection;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.smartbot.util.MinecraftLinkUtil;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DiscordMinecraftUser {

    private static final Map<Long, DiscordMinecraftUser> users = new HashMap<>();

    private final long userId;
    private final Document document;

    private DiscordMinecraftUser(long userId) {
        System.out.println("Creating DiscordMinecraftUser " + userId);
        this.userId = userId;
        this.document = loadDocumentData();
        System.out.println("Instance! " + document);
        users.put(userId, this);
    }

    public boolean isLinkedWithMinecraft() {
        return document.containsKey("minecraft-id");
    }

    public void sendMultilanguageMessage(IMessageCategory category, Object... args) {
        String key = IMessageCategory.getLanguageKeyByFieldName(category.getFieldName());
        String holderName = category.getClass().getDeclaredAnnotation(LangMessagesInfo.class).name();
        String variable = "<lang." + holderName + "." + key + ">";
        sendMessage(variable, args);
    }

    public void sendMessage(String text, Object... args) {
        String formatted = MessageFormat.format(text, args);
        Document requestDocument = new Document("_id", getMinecraftId().toString())
                .append("message", formatted);
        if (args.length >= 1) {
            requestDocument.append("args", Arrays.asList(args));
        }

        BackendClient.mainConnection.broadcastCommand("mcPlayer@" + getMinecraftId(), "sendPlayerMsg " + requestDocument.toJson());
    }

    public UUID getMinecraftId() {
        return UUID.fromString(document.getString("minecraft-id"));
    }

    private Document loadDocumentData() {
        MongoCollection<org.bson.Document> collection = MinecraftLinkUtil.getUserDataCollection();
        Document dbDocument = collection.find(MinecraftLinkUtil.getDiscordUserDataQuery(userId)).first();
        if (dbDocument == null) dbDocument = MinecraftLinkUtil.getDiscordUserDataQuery(userId);
        return dbDocument;
    }

    public static DiscordMinecraftUser get(Member member) {
        long id = member.getIdLong();
        if (users.containsKey(id)) return users.get(id);
        return new DiscordMinecraftUser(id);
    }
}
