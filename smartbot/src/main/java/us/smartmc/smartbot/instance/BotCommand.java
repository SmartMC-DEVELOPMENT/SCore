package us.smartmc.smartbot.instance;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface BotCommand {

    void execute(MessageReceivedEvent event);
    String getName();

}
