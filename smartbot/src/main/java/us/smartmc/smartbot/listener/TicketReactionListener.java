package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.TicketsHandler;
import us.smartmc.smartbot.instance.ticket.TicketActionHandler;
import us.smartmc.smartbot.manager.AutoRoleManager;

import java.nio.channels.Channel;
import java.util.Random;


public class TicketReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        Guild guild = event.getGuild();
        MessageChannelUnion channel = event.getChannel();
        String emoji = event.getReaction().getEmoji().getName();

        System.out.println("onMessageReactionAdd TicketReactionListener -> " + guild.getId() + " " + emoji);

        TicketActionHandler ticketActionHandler = TicketActionHandler.get(guild.getId(), channel.getId(), emoji);
        if (ticketActionHandler == null) {
            System.out.println("onMessageReactionAdd TicketReactionListener -> handler is null");
            return;
        }
        TicketsHandler ticketsHandler = TicketsHandler.loadTicketsHandler(guild.getId());
        if (ticketsHandler == null) {
            System.out.println("onMessageReactionAdd TicketReactionListener -> ticketsHandler is null");
            return;
        }
        String sectionID = ticketsHandler.getSectionID();
        if (sectionID == null) {
            System.out.println("onMessageReactionAdd TicketReactionListener -> sectionID is null");
            return;
        }
        Category category = guild.getCategoryById(sectionID);
        if (category == null) {
            System.out.println("onMessageReactionAdd TicketReactionListener -> section is null");
            return;
        }
        ChannelAction<TextChannel> channelAction = category.createTextChannel(ticketActionHandler.getParsedNamePlaceholder(event.getMember().getNickname(),
                String.valueOf(new Random().nextInt(10000))));
        TextChannel textChannel = channelAction.complete();
        textChannel.sendMessage("BUENO BUENO TENEMOS NUEVO TICKET DE " + event.getMember().getAsMention() + " SEÑORESSSS Y SEÑORASSSS!").queue();
    }
}
