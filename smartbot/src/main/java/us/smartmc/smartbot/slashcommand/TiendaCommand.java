package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import us.smartmc.smartbot.instance.SlashCommand;

import java.util.List;

public class TiendaCommand extends SlashCommand {

    public TiendaCommand(String name) {
        super(name, "shop", "store");
        setDescription("Link to the online shop to the active subscriptions.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Nuestra tienda: https://tienda.smartmc.us")
                .setEphemeral(true).queue();
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.DISABLED;
    }
}
