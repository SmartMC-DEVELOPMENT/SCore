package us.smartmc.smartbot.textcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.smartmc.smartbot.instance.TextCommand;

public class SendMessageCommand extends TextCommand {

    public SendMessageCommand(String name) {
        super(name);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        // EXAMPLE: !sendMessage 1109545192786231357 Esto es un mensaje de test
        Member member = event.getMember();
        if (member == null) return;
        User user = member.getUser();
        if (user.isBot()) return;
        if (!member.hasPermission(Permission.ADMINISTRATOR)) return;
        Message message = event.getMessage();
        String raw = message.getContentRaw();
        String[] args = raw.split(" ");
        String channelId = args[1];

        String messageToSend = readMessage(raw, 2);
        GuildChannel guildChannel = event.getGuild().getGuildChannelById(channelId);

        if (guildChannel instanceof GuildMessageChannel channel) {
            sendMessage(channel, messageToSend);
            event.getMessage().addReaction(Emoji.fromUnicode("✅")).queue();
        }
    }

    public static String readMessage(String raw, int indexStart) {
        String[] args = raw.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = indexStart; i < args.length; i++) {
            builder.append(args[i]);
            builder.append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static void sendMessage(GuildMessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

}
