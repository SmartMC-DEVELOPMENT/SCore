package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.EventSchedulerHandler;
import us.smartmc.smartbot.instance.SlashCommand;

import java.util.List;
import java.util.Objects;

public class AnuncioCommand extends SlashCommand {

    private static final String MESSAGE_ID_ID = "message_id";
    private static final String SELECTION_CHANNEL_ID = "select-channel";
    private static final String SELECTION_PUBLISH_AT = "publish-at";

    public AnuncioCommand(String name) {
        super(name);
        setDescription("Anuncia un mensaje en un canal y tiempo concreto (tiempo opcional).");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getGuild() == null) return;
        if (SmartBotMain.isAllowedGuild(event.getGuild()))
        if (event.getMember() == null) return;

        String messageId = Objects.requireNonNull(event.getOption(MESSAGE_ID_ID)).getAsString();

        Message referenceMessage = null;

        for (TextChannel channel : event.getGuild().getTextChannels()) {
            try {
                Message message = channel.retrieveMessageById(messageId).complete();
                if (message == null) continue;
                referenceMessage = message;
                break;
            } catch (Exception ignore) {
            }
        }

        if (referenceMessage == null) {
            event.reply("No se ha encontrado ningún mensaje con el id ``" + messageId+"``").complete();
            return;
        }

        TextChannel channelToSend = (TextChannel) event.getOption(SELECTION_CHANNEL_ID).getAsChannel();

        List<OptionMapping> list = event.getOptionsByName(SELECTION_PUBLISH_AT);
        long timestamp = list.isEmpty() ? 0 : list.get(0).getAsLong();
        Message finalReferenceMessage = referenceMessage;
        Runnable action = () -> {
            channelToSend.sendMessage(finalReferenceMessage.getContentRaw()).queue();
        };

        if (timestamp == 0) {
            event.reply("Sending...").complete();
            action.run();
        } else {
            EventSchedulerHandler.register(timestamp, action);
            event.reply("Added to queue! Executing at timestamp ``" + timestamp + "``").setEphemeral(true).queue();
        }
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, MESSAGE_ID_ID, "Specify a message id to announce")
                .setRequired(true),
                new OptionData(OptionType.CHANNEL, SELECTION_CHANNEL_ID, "Channel to send")
                        .setRequired(true),
                new OptionData(OptionType.INTEGER, SELECTION_PUBLISH_AT, "Timestamp for publish at"));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
