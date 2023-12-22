package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.util.ChatGPTUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ChatGPTListener extends ListenerAdapter {

    private static final List<String> gptEnabledChannels =
            Arrays.asList("1168167434402930698", "1168069901001424906");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        if (!event.isFromGuild()) return;
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        if (!gptEnabledChannels.contains(event.getChannel().getId())) return;
        if (event.getMessage().getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        message = message.toLowerCase();
        if ((message.contains("how") || message.contains("como")) || (message.contains("hello") || message.contains("hola") || message.contains("hey"))) {
            try {
                event.getChannel().sendTyping().queue();

                String content = getContent(event);

                CompletableFuture<String> response = ChatGPTUtil.callGPT(message, content);

                response.thenAccept(msg -> {
                    event.getMessage().reply(msg).complete();
                });


            } catch (Exception e) {
                event.getMessage().reply(e.getMessage()).complete();
            }
        }
    }

    @NotNull
    private static String getContent(MessageReceivedEvent event) {
        String content = "You respond briefly with short phrases. Your name is SmartBot. You are responding to SmartMC Network (Minecraft server) users on discord." +
                "The ip address is play.smartmc.us and you evaluate if you can help users with messages. If not then just respond a message saying 'ignore'";

        if (event.getChannel().getId().equals("1168167434402930698")) {
            content = "You know about Java programming. Your responses are discord systax messages.";
        }
        return content;
    }
}
