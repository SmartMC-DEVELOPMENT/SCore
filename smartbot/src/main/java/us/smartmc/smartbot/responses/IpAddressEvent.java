package us.smartmc.smartbot.responses;

import kotlin.text.Regex;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import us.smartmc.smartbot.instance.ReplyListener;
import us.smartmc.smartbot.util.RegexUtils;

import java.util.Objects;

public class IpAddressEvent implements ReplyListener {

    @Override
    public void accept(Message message) {
        MessageChannelUnion channel = message.getChannel();
        User author = message.getAuthor();

        final String raw = message.getContentRaw();
        final String[] ips = RegexUtils.getIpAddresses(raw);
        if (ips.length == 0) return;

        final boolean hasFakeIps = RegexUtils.hasFakeIpAddress(raw, ips);

        if (message.isFromGuild()) {
            if (hasFakeIps && !Objects.requireNonNull(message.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                message.delete().complete();
                channel.sendMessage("Por favor " + author.getAsMention() + ", ten cuidado por qué te puedo banear en 0,").complete();
            } else if (!hasFakeIps) {
                message.reply("¡Exacto " + author.getAsMention() + "! ¡Esa es nuestra dirección ip! Muchas gracias por aportar información valiosa \uD83D\uDD25").queue();
            }
        } else {
            if (hasFakeIps) message.reply("¡Que buena dirección ip! Si deseas la puedo almacenar en mi base de datos... \uD83D\uDE0F").complete();
            else message.reply("Uh! Esa dirección ip me encanta :D!").complete();
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message.getAuthor().isBot()) return;
        accept(message);
    }
}
