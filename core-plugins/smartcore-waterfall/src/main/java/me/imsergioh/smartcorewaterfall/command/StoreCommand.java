package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import me.imsergioh.smartcorewaterfall.messages.HelpMessages;
import net.md_5.bungee.api.CommandSender;

public class StoreCommand extends CoreCommand {
    public StoreCommand() {
        super("store", null, "tienda");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendFormattedList(HelpMessages.NAME, sender, "store");
    }
}