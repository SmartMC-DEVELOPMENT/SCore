package us.smartmc.smartbot.responses;

import net.dv8tion.jda.api.entities.Message;
import us.smartmc.smartbot.instance.ReplyListener;
import us.smartmc.smartbot.util.RegexUtils;

import java.util.HashMap;
import java.util.Map;

public class AddressAskEventResponse implements ReplyListener {

    @Override
    public void accept(Message message) {
        message.reply("La dirección ip es: ``play.smartmc.us``").complete();
    }

    @Override
    public void onMessage(Message message) {
        if (message.getAuthor().isBot()) return;
        String raw = message.getContentRaw().toLowerCase();

        String onlyLetters = raw.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        if (onlyLetters.split(" ").length == 1 && containsKeyword(onlyLetters, "ip")) {
            accept(message);
            return;
        }

        final String[] messageIps = RegexUtils.getIpAddresses(raw);
        if (messageIps.length > 0) return;

        if ((containsKeyword(raw, "cual") || containsKeyword(raw, "cúal")) && containsKeyword(raw, "ip")) {
            accept(message);
            return;
        }

        if ((containsKeyword(raw, "what") || containsKeyword(raw, "which")) && (containsKeyword(raw, "ip") || containsKeyword(raw, "address"))) {
            accept(message);
            return;
        }
    }

    private boolean containsKeyword(String message, String keyword) {
        return message.contains(keyword);
    }

}
