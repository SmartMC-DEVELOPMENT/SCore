package me.imsergioh.smartcorewaterfall.customcommand;

import me.imsergioh.smartcorewaterfall.instance.commandmanager.CustomCommandExecutor;
import net.md_5.bungee.api.CommandSender;

public class TestCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Test custom command executed!");
    }
}
