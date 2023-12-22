package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.instance.SlashCommand;

import java.util.List;

public class JoinToCommand extends SlashCommand {

    private static final String CHANNEL_ID = "channel_id";

    public JoinToCommand(String name) {
        super(name, "conectara", "connectto");
        setDescription("Connect to a voice channel");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getGuild() == null) return;
        if (SmartBotMain.isAllowedGuild(event.getGuild()))
            if (event.getMember() == null) return;
        Channel channel = event.getOption(CHANNEL_ID).getAsChannel();
        if (channel.getType().equals(ChannelType.VOICE)) {
            VoiceChannel voiceChannel = (VoiceChannel) channel;
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
        }
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.CHANNEL, CHANNEL_ID, "Specify a channel id to connect")
                .setRequired(true));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
