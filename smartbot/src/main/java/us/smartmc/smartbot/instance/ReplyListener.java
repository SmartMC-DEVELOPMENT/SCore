package us.smartmc.smartbot.instance;

import net.dv8tion.jda.api.entities.Message;

public interface ReplyListener {

    void accept(Message message);
    void onMessage(Message message);

}
