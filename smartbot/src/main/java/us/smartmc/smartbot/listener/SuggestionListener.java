package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.smartmc.smartbot.SmartBotMain;

import java.util.Arrays;
import java.util.List;


public class SuggestionListener extends ListenerAdapter {

    private static final List<String> suggestionsTextChannels =
            Arrays.asList("1116886024874889306");

    private static final String AGREE_UNICODE = "U+2705";
    private static final String DENY_UNICODE = "U+274C";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        if (!event.isFromGuild()) return;
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        if (!suggestionsTextChannels.contains(event.getChannel().getId())) return;
        try {
            event.getMessage().addReaction(Emoji.fromUnicode(AGREE_UNICODE)).queue();
            event.getMessage().addReaction(Emoji.fromUnicode(DENY_UNICODE)).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getUser() == null) return;
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        if (!suggestionsTextChannels.contains(event.getChannel().getId())) return;

        if (event.getEmoji().asUnicode().getAsCodepoints().equals(AGREE_UNICODE)) {
            remove(event, AGREE_UNICODE, DENY_UNICODE);
        } else if (event.getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase(DENY_UNICODE)){
            remove(event, DENY_UNICODE, AGREE_UNICODE);
        }
    }

    public void remove(MessageReactionAddEvent event, String ifUnicode, String removeUnicode) {
        if (event.getUser() == null) return;
        if (event.getUser().isBot()) return;
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        if (message == null) return;
        message.retrieveReactionUsers(Emoji.fromUnicode(ifUnicode)).complete();
        message.retrieveReactionUsers(Emoji.fromUnicode(removeUnicode)).complete();

        if (event.getEmoji().asUnicode().getAsCodepoints().equalsIgnoreCase(ifUnicode)) {
            for (MessageReaction reaction : message.getReactions()) {
                if (reaction.getEmoji().asUnicode().getAsCodepoints().equals(event.getReaction().getEmoji().asUnicode().getAsCodepoints())) continue;
                reaction.removeReaction(event.getUser()).complete();
            }
        }
    }

}
