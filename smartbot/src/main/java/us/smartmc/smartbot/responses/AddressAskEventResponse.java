package us.smartmc.smartbot.responses;

import net.dv8tion.jda.api.entities.Message;
import us.smartmc.smartbot.instance.ReplyListener;
import us.smartmc.smartbot.util.RegexUtils;

public class AddressAskEventResponse implements ReplyListener {

    @Override
    public void accept(Message message) {
        message.reply("La dirección ip es: ``play.smartmc.us``").complete();
    }

    @Override
    public void onMessage(Message message) {
        if (message.getAuthor().isBot()) return;
        String raw = message.getContentRaw().toLowerCase();

        final String[] messageIps = RegexUtils.getIpAddresses(raw);
        if (messageIps.length > 0) return;

        if ((containsKeyword(raw, " ip ") || containsKeyword(raw, " ip")) ||
                (containsKeyword(raw, "dirección ip") || containsKeyword(raw, "direccion ip"))||
                containsKeyword(raw, "ip address") ||
                (containsKeyword(raw, "address") && (containsKeyword(raw, "ip") || containsKeyword(raw, "server"))) ||
                ((containsKeyword(raw, "conectar") || containsKeyword(raw, "connect"))  && (containsKeyword(raw, "como")) || (containsKeyword(raw, "how")))) {
            accept(message);
        }
    }

    private boolean containsKeyword(String message, String keyword) {
        return message.contains(keyword);
    }

}
