package us.smartmc.smartbot.logfunction;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.bson.Document;
import us.smartmc.smartbot.connection.RedisConnection;
import us.smartmc.smartbot.instance.log.LogChannelRegistry;

import java.awt.*;
import java.lang.reflect.Field;

public class SendEmbedMessages extends LogChannelRegistry {

    public SendEmbedMessages() {
        super("1208514273899184130", "discord-logs:embed");
        run();
    }

    @Override
    public void onLogRegistryReceive(GuildChannel channel, String message) {
        try {
            Document document = Document.parse(message);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            String title = document.getString("title");
            String description = document.getString("description");
            String author = document.getString("author");
            String color = document.getString("color");

            String url = document.getString("url");
            String image = document.getString("image");

            if (title != null) embedBuilder.setTitle(title);
            if (description != null) embedBuilder.setDescription(description);
            if (author != null) embedBuilder.setAuthor(author);
            if (color != null) {
                Field field = Color.class.getDeclaredField(color);
                embedBuilder.setColor((Color) field.get(null));
            }

            if (url != null) embedBuilder.setUrl(url);
            if (image != null) embedBuilder.setImage(image);

            if (document.containsKey("fields")) {
                for (Document fieldDocument : document.getList("fields", Document.class)) {
                    String key = fieldDocument.getString("key");
                    String value = fieldDocument.getString("value");
                    boolean inLine = fieldDocument.getBoolean("inLine", false);
                    embedBuilder.addField(key, value, inLine);
                }
            }
            if (channel instanceof TextChannel textChannel) {
                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
