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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class TicketSavementListeners extends ListenerAdapter {

    private Set<String> saving = new HashSet<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (!(event.getChannel() instanceof TextChannel)) return;
        TextChannel channel = (TextChannel) event.getChannel();
        TicketStorageSaver saver = TicketStorageSaver.getByChannelId(channel);
        if (saver == null) return;
        saver.registerMessage(event.getMessage());
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (Objects.requireNonNull(event.getUser()).isBot()) return;
        if (!(event.getChannel() instanceof TextChannel)) return;
        TextChannel textChannel = (TextChannel) event.getChannel();
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        Guild guild = event.getGuild();
        TicketsHandler ticketsHandler = TicketsHandler.loadTicketsHandler(guild.getId());
        if (ticketsHandler == null) return;
        String handlerSectionID = ticketsHandler.getSectionID();
        Category category = event.getChannel().asTextChannel().getParentCategory();
        if (category == null) return;
        if (!category.getId().equals(handlerSectionID)) return;
        if (event.getMessageId().equals(TicketStorageSaver.getByChannelId(textChannel).getInitialMessage().getId())) {
            event.getChannel().delete().queue();
        }
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (!event.isFromGuild()) return;
        if (!(event.getChannel() instanceof TextChannel)) return;
        TextChannel channel = (TextChannel) event.getChannel();
        save(event.getGuild(), channel);
    }

    private void save(Guild guild, TextChannel channel) {
        if (!saving.add(channel.getId())) return;
        TicketsHandler.removeCacheTicket(channel.getId());
        System.out.println("Saving ticket " + guild.getId() + " " + channel.getId());
        TicketStorageSaver saver = TicketStorageSaver.getByChannelId(channel);
        saver.save();
        saving.remove(channel.getId());
    }
}
