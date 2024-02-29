package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.instance.SlashCommand;

import java.util.List;
import java.util.Objects;

public class ReactToCommand extends SlashCommand {

    private static final String CHANNEL_ID = "channel_id";
    private static final String MESSAGE_ID = "message_id";
    private static final String EMOJI_ID = "emoji_id";

    public ReactToCommand(String name) {
        super(name, "react", "reactmessage");
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
        Message message = channel.getHistory().getMessageById(messageId);
        String emojiId = Objects.requireNonNull(event.getOption(EMOJI_ID)).getAsString();
        Emoji emoji = event.getGuild().getEmojiById(EMOJI_ID);
        if (message == null || emoji == null) return;
        message.addReaction(emoji).queue();
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.CHANNEL, CHANNEL_ID, "Specify the channel of message") ,
                        new OptionData(OptionType.STRING, MESSAGE_ID, "Specify the message id of message"),
                new OptionData(OptionType.STRING, EMOJI_ID, "Specify the emoji id/name you want for react to the message")
                .setRequired(true));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
