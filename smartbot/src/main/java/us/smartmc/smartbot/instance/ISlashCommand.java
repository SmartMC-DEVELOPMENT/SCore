package us.smartmc.smartbot.instance;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Set;

public interface ISlashCommand extends BotCommand {

    List<OptionData> getOptions();

    DefaultMemberPermissions getDefaultPermission();

    void execute(SlashCommandInteractionEvent event);

    String getDescription();

    Set<String> getAliases();
}
