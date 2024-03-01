package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.TicketActionHandler;
import us.smartmc.smartbot.handler.TicketsHandler;

import java.util.Objects;
import java.util.Random;


public class TicketReactionListener extends ListenerAdapter {

    private static final String TICKET_ID_FORMAT = "xxxxxx";

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        Guild guild = event.getGuild();
        MessageChannelUnion channel = event.getChannel();
        String emoji = event.getReaction().getEmoji().getName();

        TicketActionHandler ticketActionHandler = TicketActionHandler.get(guild.getId(), channel.getId(), emoji);
        if(ticketActionHandler == null) return;
        TicketsHandler ticketsHandler = TicketsHandler.loadTicketsHandler(guild.getId());
        if(ticketsHandler == null) return;
        String sectionID = ticketsHandler.getSectionID();
        Category category = guild.getCategoryById(sectionID);
        User user = event.getUser();
        if (user == null) return;
        String username = user.getName();
        if (category == null) return;
        String ticketID = generateTicketID();
        ChannelAction<TextChannel> channelAction = category.createTextChannel(ticketActionHandler.getParsedNamePlaceholder(username, ticketID));
        TextChannel textChannel = channelAction.complete();
        textChannel.sendMessage(
                "¡Hola " + Objects.requireNonNull(event.getMember()).getAsMention() + ", acabas de crear un ticket de " + ticketActionHandler.getChannelPrefix() + "!\n" +
                        "Espera a que te atiendan por favor. " + getMenction(guild, ticketActionHandler) + "\n" +
                        "Reacciona: ❌ para eliminar").queue();
        TicketsHandler.registerTicket(ticketID, event);
    }

    public static String getMenction(Guild guild, TicketActionHandler handler) {
        String id = handler.getRoleMenction();
        if (id == null) return "";
        Role role = guild.getRoleById(id);
        if (role == null) return "";
        return role.getAsMention();
    }

    public static String generateTicketID() {
        String ticketID = TICKET_ID_FORMAT;
        while (ticketID.contains("x")) {
            ticketID = ticketID.replaceFirst("x", String.valueOf(new Random().nextInt(9)));
        }
        return ticketID;
    }

}
