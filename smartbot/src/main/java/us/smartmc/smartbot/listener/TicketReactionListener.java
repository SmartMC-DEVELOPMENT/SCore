package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.handler.TicketActionHandler;
import us.smartmc.smartbot.handler.TicketsHandler;
import us.smartmc.smartbot.instance.ticket.TicketStorageSaver;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


public class TicketReactionListener extends ListenerAdapter {

    private static final String TICKET_ID_FORMAT = "xxxxxx";

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (!event.isFromGuild()) return;
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;
        Guild guild = event.getGuild();
        MessageChannelUnion channel = event.getChannel();
        Message message = channel.retrieveMessageById(event.getMessageId()).complete();
        String emoji = event.getReaction().getEmoji().getName();
        if (event.getUser().isBot()) return;

        TicketActionHandler ticketActionHandler = TicketActionHandler.get(guild.getId(), channel.getId(), emoji);
        if(ticketActionHandler == null) return;
        TicketsHandler ticketsHandler = TicketsHandler.loadTicketsHandler(guild.getId());
        if(ticketsHandler == null) return;

        message.removeReaction(event.getReaction().getEmoji(), Objects.requireNonNull(event.getUser())).queue();
        String sectionID = ticketsHandler.getSectionID();
        Category category = guild.getCategoryById(sectionID);
        User user = event.getUser();
        if (user == null) return;
        String username = user.getName();
        if (category == null) return;
        if (TicketsHandler.isActiveUserTicketDelay(user.getId())) return;
        String ticketID = generateTicketID();
        ChannelAction<TextChannel> channelAction = category.createTextChannel(ticketActionHandler.getParsedNamePlaceholder(username, ticketID));
        Member member = event.getMember();
        if (member == null) return;

        TextChannel textChannel = channelAction.complete();
        CompletableFuture.supplyAsync(() -> textChannel.sendMessage(
                "¡Hola " + Objects.requireNonNull(event.getMember()).getAsMention() + ", acabas de crear un ticket de " + ticketActionHandler.getChannelPrefix() + "!\n" +
                        "Espera a que te atiendan por favor. " + getMenction(guild, ticketActionHandler) + "\n" +
                        "Reacciona: ❌ para eliminar").complete()).thenAccept(welcomeMessage -> {
            welcomeMessage.addReaction(Emoji.fromUnicode("U+274C")).queue();
            TicketsHandler.registerTicket(ticketID, textChannel.getId(), event);
            TicketsHandler.registerUserTicketDelay(event.getUser());
            TicketStorageSaver.registerTicketSaver(welcomeMessage, textChannel);
        }).thenRun(() -> {
            textChannel.upsertPermissionOverride(Objects.requireNonNull(guild.getRoleById(ticketActionHandler.getRoleMenction()))).grant(Permission.VIEW_CHANNEL).queue();
            textChannel.upsertPermissionOverride(member).grant(Permission.VIEW_CHANNEL).queue();
        });
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
