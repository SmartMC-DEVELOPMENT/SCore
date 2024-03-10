package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurveysListener extends ListenerAdapter {

    private static final String CHANNEL_ID = "1135095627039113246";

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.getChannel().getId().equals(CHANNEL_ID)) return;
        removeLastReactions(event, event.getUser());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        perform(event, event.getAuthor(), event.getMessage());
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        perform(event, event.getAuthor(), event.getMessage());
    }

    public void perform(GenericMessageEvent event, User user, Message message) {
        if (!event.isFromGuild()) return;
        if (!event.getChannel().getId().equals(CHANNEL_ID)) return;
        if (user.isBot()) return;
        try {
            List<Emoji> emojis = extractEmojis(message);
            for (Emoji emoji : emojis) {
                message.addReaction(emoji).complete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Emoji> extractEmojis(Message message) {
        Guild guild = message.getGuild();
        List<Emoji> emojis = new ArrayList<>();
        String contentRaw = message.getContentRaw();

        System.out.println("Exctracting emojis from " + contentRaw);

        // Pattern for custom emotes <:emote_name:id>
        Pattern emotePattern = Pattern.compile("<:[a-zA-Z0-9_]+:\\d+>");
        Matcher emoteMatcher = emotePattern.matcher(contentRaw);
        while (emoteMatcher.find()) {
            try {
                String name = emoteMatcher.group();
                String id = name.split(":")[2].replace(">", "");
                emojis.add(guild.retrieveEmojiById(id).complete());
            } catch (Exception ignore) {
            }
        }

        // Pattern for Unicode emojis
        Pattern emojiPattern = Pattern.compile("[\\x{10000}-\\x{10FFFF}✅❌]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emojiPattern.matcher(contentRaw);
        while (emojiMatcher.find()) {
            try {
                String emoji = emojiMatcher.group();
                emojis.add(Emoji.fromFormatted(emoji));
            } catch (Exception ignore) {
            }
        }
        return emojis;
    }

    public void removeLastReactions(MessageReactionAddEvent event, User user) {
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        for (MessageReaction reaction : message.getReactions()) {
            if (reaction.getEmoji().equals(event.getEmoji())) continue;
            for (User reactionUser : reaction.retrieveUsers()) {
                if (reactionUser.getId().equals(user.getId())) {
                    reaction.removeReaction(user).complete();
                }
            }
        }
    }

}
