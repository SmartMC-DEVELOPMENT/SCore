package us.smartmc.smartbot.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.instance.BotCommand;
import us.smartmc.smartbot.instance.SlashCommand;
import us.smartmc.smartbot.instance.TextCommand;

import java.util.HashMap;
import java.util.List;

public class CommandHandler {

    private static final HashMap<String, BotCommand> commands = new HashMap<>();

    public static void clearCommands() {
        SmartBotMain.getJDA().retrieveCommands().complete().forEach(cmd -> SmartBotMain.getJDA().deleteCommandById(cmd.getId()).complete());
    }

    public static void executeTextCommand(MessageReceivedEvent event) {
        String name = event.getMessage().getContentRaw().split(" ")[0];
        BotCommand botCommand = commands.get(name);
        if (botCommand instanceof TextCommand) {
            TextCommand cmd = (TextCommand) botCommand;
            cmd.execute(event);
        }
    }

    public static void executeSlashCommand(SlashCommandInteractionEvent event) {
        BotCommand botCommand = commands.get(event.getName());
        if (botCommand instanceof SlashCommand) {
            SlashCommand cmd = (SlashCommand) botCommand;
            cmd.execute(event);
        }
    }

    public static void register(BotCommand... cmds) {
        for (BotCommand command : cmds) {
            commands.put(command.getName(), command);
            // Register slash command
            if (command instanceof SlashCommand) {
                SlashCommand cmd = (SlashCommand) command;
                registerSlashCommand(cmd);
                SlashCommandData commandData = Commands.slash(cmd.getName(), cmd.getDescription())
                                .setDefaultPermissions(cmd.getDefaultPermission());
                List<OptionData> options = cmd.getOptions();
                if (options != null) {
                    commandData.addOptions(options);
                }
                SmartBotMain.getMainGuild().upsertCommand(commandData).complete();
                SmartBotMain.getJDA().addEventListener(cmd);
            }
        }
        SmartBotMain.getJDA().updateCommands().complete();
    }

    private static void registerSlashCommand(SlashCommand command) {
        for (String alias : command.getAliases()) {
            commands.put(alias, command);
            //SmartBotMain.getJDA().upsertCommand(alias, command.getDescription()).queue();
        }
    }

    public static BotCommand get(String name) {
        return commands.get(name);
    }

}
