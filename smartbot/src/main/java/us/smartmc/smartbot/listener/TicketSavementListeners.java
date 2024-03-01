package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.TicketsHandler;
import us.smartmc.smartbot.instance.ticket.TicketStorageSaver;

import java.util.Objects;


public class TicketSavementListeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (!(event.getChannel() instanceof TextChannel channel)) return;
        TicketStorageSaver saver = TicketStorageSaver.getByChannelId(event.getGuild(), channel);
        if (saver == null) return;
        saver.registerMessage(event.getMessage());
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (Objects.requireNonNull(event.getUser()).isBot()) return;
        if (!(event.getChannel() instanceof TextChannel textChannel)) return;
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        Guild guild = event.getGuild();
        TicketsHandler ticketsHandler = TicketsHandler.loadTicketsHandler(guild.getId());
        if (ticketsHandler == null) return;
        String handlerSectionID = ticketsHandler.getSectionID();
        Category category = event.getChannel().asTextChannel().getParentCategory();
        if (category == null) return;
        if (!category.getId().equals(handlerSectionID)) return;
        save(event.getGuild(), textChannel);
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (!event.isFromGuild()) return;
        if (!(event.getChannel() instanceof TextChannel channel)) return;
        save(event.getGuild(), channel);
    }

    private void save(Guild guild, TextChannel channel) {
        System.out.println("Saving ticket " + guild.getId() + " " + channel.getId());
        TicketStorageSaver saver = TicketStorageSaver.getByChannelId(guild, channel);
        saver.save();
    }
}
