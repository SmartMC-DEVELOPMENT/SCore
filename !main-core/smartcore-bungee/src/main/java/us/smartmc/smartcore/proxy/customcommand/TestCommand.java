package us.smartmc.smartcore.proxy.customcommand;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import us.smartmc.smartcore.proxy.instance.commandmanager.CustomCommandExecutor;

public class TestCommand implements CustomCommandExecutor {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage((BaseComponent) Component.text("Test custom command executed!"));
    }
}
