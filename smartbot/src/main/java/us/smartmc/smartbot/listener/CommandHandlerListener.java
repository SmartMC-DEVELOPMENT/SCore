package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.CommandHandler;

public class CommandHandlerListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        Guild guild = event.getGuild();
        if (guild == null) return;
        if (!SmartBotMain.isAllowedGuild(guild)) return;
        CommandHandler.executeSlashCommand(event);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        Guild guild = event.getGuild();
        if (!SmartBotMain.isAllowedGuild(guild)) return;
        CommandHandler.executeTextCommand(event);
    }
}
