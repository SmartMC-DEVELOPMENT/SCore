package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.instance.SlashCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReactToCommand extends SlashCommand {

    private static final String CHANNEL_ID = "channel_id";
    private static final String MESSAGE_ID = "message_id";
    private static final String EMOJI_ID = "emoji_id";

    public ReactToCommand() {
        super("reactto", "react", "reactmessage");
        setDescription("React to a given message");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getGuild() == null) return;
        if (SmartBotMain.isAllowedGuild(event.getGuild()))
            if (event.getMember() == null) return;
        TextChannel channel = (TextChannel) Objects.requireNonNull(event.getOption(CHANNEL_ID)).getAsChannel();
        String messageId = Objects.requireNonNull(event.getOption(MESSAGE_ID)).getAsString();
        Message message = channel.retrieveMessageById(messageId).complete();
        String emojiName = Objects.requireNonNull(event.getOption(EMOJI_ID)).getAsString();
        System.out.println("EmojiName=" + emojiName);
        Emoji emoji = getEmojiByName(event.getGuild(), emojiName);
        if (emoji == null) emoji = Emoji.fromUnicode(emojiName);

        message.addReaction(emoji).complete();
        event.reply("Reaction added!").setEphemeral(true).complete();
    }

    private RichCustomEmoji getEmojiByName(Guild guild, String name) {
        List<RichCustomEmoji> list = guild.getEmojisByName(name, true);
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    @Override
    public List<OptionData> getOptions() {
        return Arrays.asList(new OptionData(OptionType.CHANNEL, CHANNEL_ID, "Specify the channel of message").setRequired(true),
                new OptionData(OptionType.STRING, MESSAGE_ID, "Specify the message id of message").setRequired(true),
                new OptionData(OptionType.STRING, EMOJI_ID, "Specify the emoji id/name you want for react to the message")
                        .setRequired(true));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
