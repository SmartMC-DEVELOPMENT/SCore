package us.smartmc.smartbot.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.connection.RedisConnection;
import us.smartmc.smartbot.instance.SlashCommand;
import us.smartmc.smartbot.instance.linkdiscord.SaveMinecraftLinkVinculation;
import us.smartmc.smartbot.instance.minecraft.DiscordMinecraftUser;
import us.smartmc.smartbot.message.MainMessages;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class VerifyMinecraftLinkCommand extends SlashCommand {

    private static final String LINK_CODE_ID = "link_code";

    public VerifyMinecraftLinkCommand(String name) {
        super(name, "verifyminecraft", "linkminecraft", "verificarmc", "verificarminecraft");
        setDescription("Link your minecraft account with discord to enjoy a better experience in SmartMC");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getGuild() == null) return;
        if (SmartBotMain.isAllowedGuild(event.getGuild()))
            if (event.getMember() == null) return;
        String code = Objects.requireNonNull(event.getOption(LINK_CODE_ID)).getAsString();
        String key = "linkDiscord." + code;
        String id = RedisConnection.mainConnection.getResource().get(key);
        if (id == null) {
            event.reply("El código proporcionado no es válido").setEphemeral(true).complete();
            return;
        } else {
            new SaveMinecraftLinkVinculation(code, Objects.requireNonNull(event.getMember()), UUID.fromString(id));
            event.reply("Muchas gracias! Se ha vinculado tu Discord correctamente!").setEphemeral(true).complete();

            DiscordMinecraftUser.get(event.getMember()).sendMultilanguageMessage(MainMessages.LINKED_DISCORD_SUCCESSFULLY, event.getMember().getUser().getName());
        }
    }

    @Override
    public List<OptionData> getOptions() {
        return Arrays.asList(new OptionData(OptionType.STRING, LINK_CODE_ID, "Specify the link code provided by Minecraft Server")
                .setRequired(true));
    }

    @Override
    public DefaultMemberPermissions getDefaultPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.EMPTY_PERMISSIONS);
    }
}
