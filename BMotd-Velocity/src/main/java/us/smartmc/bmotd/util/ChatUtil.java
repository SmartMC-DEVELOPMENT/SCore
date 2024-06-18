package us.smartmc.bmotd.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.text.MessageFormat;
import java.util.Objects;

public class ChatUtil {

    public static String color(String message, Object... args) {
        return LegacyComponentSerializer
                .builder()
                .build()
                .deserialize(Objects.requireNonNull(MessageFormat.format(message, args)))
                .insertion();
    }

    public static String parse(String message, Object... args) {
        return LegacyComponentSerializer
                .builder()
                .build()
                .deserialize(Objects.requireNonNull(MessageFormat.format(message, args)))
                .insertion();
    }

    public static Component parseToComponent(String message, Object... args) {
        message = MessageFormat.format(message, args);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(message);
    }

}
