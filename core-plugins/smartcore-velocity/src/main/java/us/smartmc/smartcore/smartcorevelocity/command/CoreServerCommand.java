package us.smartmc.smartcore.smartcorevelocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.proxy.command.builtin.ServerCommand;
import me.imsergioh.pluginsapi.VelocityCore;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;
import us.smartmc.smartcore.velocitycore.manager.VelocityPluginsAPI;

public class CoreServerCommand extends CoreCommand {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    private final com.velocitypowered.proxy.command.builtin.ServerCommand defaultCommand;

    public CoreServerCommand() {
        super("server");
        defaultCommand = new com.velocitypowered.proxy.command.builtin.ServerCommand();
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("*")) {
            Plugin info = SmartCoreVelocity.class.getAnnotation(Plugin.class);
            sender.sendMessage(Component.text(ChatUtil.parse("&bSmartCoreVelocity plugin v " +
                    info.version() + " by ImSergioh!")));
            return;
        }
        ServerCommand.create(VelocityPluginsAPI.proxy);
    }
}
