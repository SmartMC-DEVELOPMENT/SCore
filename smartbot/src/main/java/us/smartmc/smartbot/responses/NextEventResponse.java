package us.smartmc.smartbot.responses;

import net.dv8tion.jda.api.entities.Message;
import us.smartmc.smartbot.instance.ReplyListener;

public class NextEventResponse implements ReplyListener {

    @Override
    public void accept(Message message) {
        message.reply("El evento de apertura es el <t:1696008599:f>").complete();
    }

    @Override
    public void onMessage(Message message) {
        String raw = message.getContentRaw().toLowerCase();
        if (raw.contains("event") && (raw.contains("cuando") || raw.contains("cuándo") || raw.contains("when"))) {
            accept(message);
        }
    }
}
