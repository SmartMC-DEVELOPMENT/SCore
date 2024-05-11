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

public class RemoveQuoteCommand extends SlashCommand {

    private static final String QUOTE_ID = "quote_id";

    public RemoveQuoteCommand() {
        super("removequote");
        setDescription("Publica en la Network un comando del día");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getGuild() == null) return;
        if (SmartBotMain.isAllowedGuild(event.getGuild()))
            if (event.getMember() == null) return;

        long quoteId = Objects.requireNonNull(event.getOption(QUOTE_ID)).getAsLong();
        CustomProxyCommandManager.remove(quoteId);
        event.reply("Se ha removido la cita para los comandos identificados con: " + quoteId).queue();
    }

    @Override
    public List<OptionData> getOptions() {
        return Arrays.asList(
                new OptionData(OptionType.INTEGER, QUOTE_ID, "Specify id of quote you want to remove")
                        .setRequired(true));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
