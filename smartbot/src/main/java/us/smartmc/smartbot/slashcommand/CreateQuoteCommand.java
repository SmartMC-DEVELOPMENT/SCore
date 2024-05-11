package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.instance.SlashCommand;
import us.smartmc.smartbot.manager.CustomProxyCommandManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateQuoteCommand extends SlashCommand {

    private static final String COMMAND_ID = "command_id";
    private static final String COMMAND_INSTRUCTION = "command_instruction";
    private static final String END_TIMESTAMP = "end_timestamp";

    public CreateQuoteCommand() {
        super("createquote");
        setDescription("Publica en la Network un comando del día");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getGuild() == null) return;
        if (SmartBotMain.isAllowedGuild(event.getGuild()))
            if (event.getMember() == null) return;

        String commandId = Objects.requireNonNull(event.getOption(COMMAND_ID)).getAsString();
        String commandInstruction = Objects.requireNonNull(event.getOption(COMMAND_INSTRUCTION)).getAsString();
        long defaultEndDate = (System.currentTimeMillis() / 1000) + 86400;
        long endDate = event.getOption(END_TIMESTAMP) == null ? defaultEndDate : Objects.requireNonNull(event.getOption(END_TIMESTAMP)).getAsLong();
        String id = String.valueOf(endDate);

        CustomProxyCommandManager.register(endDate, commandId, commandInstruction);
        event.reply("Se ha creado una cita para el comando /" + commandId + ", instrucción establecida a ``" + commandInstruction + "``.\nTerminación de la cita: " + endDate + "\nIdentificador: " + id)
                .queue();
    }

    @Override
    public List<OptionData> getOptions() {
        return Arrays.asList(
                new OptionData(OptionType.STRING, COMMAND_ID, "Specify cmd id you want to add")
                        .setRequired(true),
                new OptionData(OptionType.STRING, COMMAND_INSTRUCTION, "Specify command you want to set to the command")
                        .setRequired(true),
                new OptionData(OptionType.INTEGER, END_TIMESTAMP, "Specify end date as timestamp if you want to just end it before 24h")
                        .setRequired(false));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
