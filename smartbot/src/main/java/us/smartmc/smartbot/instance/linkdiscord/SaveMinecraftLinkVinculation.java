package us.smartmc.smartbot.instance.linkdiscord;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import us.smartmc.smartbot.connection.RedisConnection;
import us.smartmc.smartbot.util.MinecraftLinkUtil;

import java.util.UUID;

public class SaveMinecraftLinkVinculation {

    private final Member member;
    private final UUID minecraftId;

    public SaveMinecraftLinkVinculation(String code, Member member, UUID id) {
        this.member = member;
        this.minecraftId = id;
        RedisConnection.mainConnection.getResource().del("linkDiscord." + code);
        MongoCollection<Document> collection = MinecraftLinkUtil.getUserDataCollection();
        collection.deleteMany(MinecraftLinkUtil.getDiscordUserDataQuery(member.getIdLong()));
        collection.insertOne(getSetupDocument());
    }

    private Document getSetupDocument() {
        return MinecraftLinkUtil.getDiscordUserDataQuery(member.getIdLong())
                .append("effectiveName", member.getEffectiveName())
                .append("globalName", member.getUser().getGlobalName())
                .append("name", member.getUser().getName())
                .append("minecraft-id", minecraftId.toString());
    }
}
