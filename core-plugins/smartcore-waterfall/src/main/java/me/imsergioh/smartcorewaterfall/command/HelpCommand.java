package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class HelpCommand extends CoreCommand {
    public HelpCommand() {
        super("help", null, "ayuda");
    }

    private final ComponentBuilder builder = new ComponentBuilder("");

    @Override
    public void execute(CommandSender sender, String[] args) {
        builder.append("");
        builder.append(ChatUtil.parse("&b&lSmart&f&lMC &eNetwork &8| &fHelp Command"));
        builder.append("");
        builder.append(ChatUtil.parse("&a1. Server Info")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "info"));
        builder.append(ChatUtil.parse("&a2. Utility Links")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "links"));
        builder.append(ChatUtil.parse("&a3. Basic Commands")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "commands"));
        builder.append(ChatUtil.parse("&c&m4. Discord Sync"));
        builder.append("");
        builder.append(ChatUtil.parse("&7&oClick on any of these options to select"));
        builder.append("");
    }
}
