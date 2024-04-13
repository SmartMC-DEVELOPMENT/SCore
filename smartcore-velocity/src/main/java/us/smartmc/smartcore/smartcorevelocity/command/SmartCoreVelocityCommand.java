package us.smartmc.smartcore.smartcorevelocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;

import java.lang.annotation.Annotation;

public class SmartCoreVelocityCommand extends CoreCommand {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    public SmartCoreVelocityCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("*")) {
            Plugin info = SmartCoreVelocity.class.getAnnotation(Plugin.class);
            sender.sendMessage(VelocityChatUtil.parse("&bSmartCoreWaterfall plugin v " +
                    info.version() + " by ImSergioh!"));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(Component.text("Available Subcommands:\nreloadConfig\n\nreloadCustomCmds\nreloadMessages\nreloadTabs"));
            return;
        }

        if (args[0].contains("config")) {
            plugin.loadConfig();
            sender.sendMessage(VelocityChatUtil.parse("&aConfiguración recargada!"));
        }

        if (args[0].contains("cmd")) {
            CustomCommandsManager.unregisterAll();
            plugin.loadCustomCommands();
            CustomCommandsManager.forEach(customCommandsManager -> {
                customCommandsManager.load();
                sender.sendMessage(VelocityChatUtil.parse("&aRecargado: &e" + customCommandsManager.getName()));
            });
            sender.sendMessage(VelocityChatUtil.parse("&aRecargados!"));
        }

        if (args[0].contains("mess") || args[0].contains("msg")) {
            plugin.loadMessages();
            sender.sendMessage(VelocityChatUtil.parse("&aMensajes Recargados!"));
        }

        if (args[0].contains("tab")) {
            TabHandler.register();
            sender.sendMessage(VelocityChatUtil.parse("&aTabs Recargados!"));
        }
    }
}
