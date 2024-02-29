package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.TicketsHandler;
import us.smartmc.smartbot.instance.ticket.TicketStorageSaver;


public class TicketStoreListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!(event.getChannel() instanceof TextChannel textChannel)) return;
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        Guild guild = event.getGuild();
        TicketsHandler ticketsHandler = TicketsHandler.loadTicketsHandler(guild.getId());
        assert ticketsHandler != null;
        String handlerSectionID = ticketsHandler.getSectionID();
        Category category = event.getChannel().asTextChannel().getParentCategory();
        assert category != null;
        if (!category.getId().equals(handlerSectionID)) return;
        Document ticketIdDoc = TicketsHandler.getTicketIdentifier(event.getChannel().getId());
        if (ticketIdDoc == null) return;
        new TicketStorageSaver(ticketIdDoc, textChannel);
    }
}
