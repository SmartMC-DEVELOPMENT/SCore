package us.smartmc.smartbot.textcommand;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.smartmc.smartbot.instance.TextCommand;

public class TestCommand extends TextCommand {

    public TestCommand(String name) {
        super(name);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getMessage().addReaction(Emoji.fromUnicode("U+1FAE1")).queue();
    }
}
