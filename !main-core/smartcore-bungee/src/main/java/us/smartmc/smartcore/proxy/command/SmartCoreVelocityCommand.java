package us.smartmc.smartcore.proxy.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import us.smartmc.smartcore.proxy.instance.CoreCommand;
import us.smartmc.smartcore.proxy.manager.AllowedCommandsManager;
import us.smartmc.smartcore.proxy.manager.CustomCommandsManager;
import us.smartmc.smartcore.proxy.manager.TabHandler;

public class SmartCoreVelocityCommand extends CoreCommand {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    public SmartCoreVelocityCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(ChatUtil.parse("&bSmartCore plugin v " +
                    plugin.getDescription().getVersion() + " by ImSergioh!"));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(new TextComponent("Available Subcommands:\nreloadConfig\n\nreloadCustomCmds\nreloadMessages\nreloadTabs"));
            return;
        }

        if (args[0].contains("config")) {
            plugin.loadConfig();
            sender.sendMessage(ChatUtil.parse("&aConfiguración recargada!"));
        }

        if (args[0].contains("cmd")) {
            CustomCommandsManager.unregisterAll();
            plugin.loadCustomCommands();
            CustomCommandsManager.forEach(customCommandsManager -> {
                customCommandsManager.load();
                sender.sendMessage(ChatUtil.parse("&aCustomCommands recargados: &e" + customCommandsManager.getName()));
            });

            AllowedCommandsManager.forEach(customCommandsManager -> {
                customCommandsManager.load();
                sender.sendMessage(ChatUtil.parse("&aAllowedCommands recargados: &e" + customCommandsManager.getName()));
            });
            sender.sendMessage(ChatUtil.parse("&aRecargados!"));
        }

        if (args[0].contains("mess") || args[0].contains("msg")) {
            plugin.loadMessages();
            sender.sendMessage(ChatUtil.parse("&aMensajes Recargados!"));
        }

        if (args[0].contains("tab")) {
            TabHandler.register();
            sender.sendMessage(ChatUtil.parse("&aTabs Recargados!"));
        }
    }
}
