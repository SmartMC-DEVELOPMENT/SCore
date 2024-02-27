package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.builder.DiscordLogEmbedBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static Set<String> alreadySent = new HashSet<>();

    private final Map<String, String> additionalFields = new HashMap<>();

    public ExceptionHandler addField(String name, String value) {
        additionalFields.put(name, value);
        return this;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handle(t, e);
    }

    private void handle(Thread thread, Throwable e) {
        if (alreadySent.contains(e.getLocalizedMessage())) return;
        DiscordLogEmbedBuilder builder = new DiscordLogEmbedBuilder();
        builder.title("New exception! (Thread: " + thread.getName() + ")")
                .addField("Message", e.getMessage())
                .addField("Localized Message", e.getLocalizedMessage());
        for (String fieldName : additionalFields.keySet()) {
            builder.addField(fieldName, additionalFields.get(fieldName));
        }
        builder.send("discord-logs:exception", RedisConnection.mainConnection.getResource());
        alreadySent.add(e.getMessage());
    }

}
