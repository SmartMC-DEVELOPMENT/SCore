package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import me.imsergioh.smartcorewaterfall.messages.HelpMessages;
import net.md_5.bungee.api.CommandSender;

public class TwitterCommand extends CoreCommand {
    public TwitterCommand() {
        super("twitter", null, "x");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendFormattedList(HelpMessages.NAME, sender, "twitter");
    }
}