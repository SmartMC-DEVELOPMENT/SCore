package us.smartmc.smartcore.smartcorevelocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.proxy.command.builtin.ServerCommand;
import me.imsergioh.pluginsapi.VelocityCore;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;
import us.smartmc.smartcore.velocitycore.manager.VelocityPluginsAPI;

import java.util.Optional;

public class CoreServerCommand extends CoreCommand {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    public CoreServerCommand() {
        super("server");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("*")) {
            Plugin info = SmartCoreVelocity.class.getAnnotation(Plugin.class);
            sender.sendMessage(Component.text(ChatUtil.parse("&bSmartCoreVelocity plugin v " +
                    info.version() + " by ImSergioh!")));
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(Component.text(ChatUtil.color("&cTienes que especificar a que server te quieres conectar")));
            return;
        }

        String serverTargetName = args[0];
        Optional<RegisteredServer> server = VelocityPluginsAPI.proxy.getServer(serverTargetName);
        server.ifPresentOrElse(registeredServer -> {
            player.sendMessage(Component.text(ChatUtil.color("&aConectando a &e" + registeredServer.getServerInfo().getName() + "&a...")));
            player.createConnectionRequest(registeredServer).fireAndForget();
        }, () -> {
            sender.sendMessage(Component.text(ChatUtil.color("&cNo se ha encontrado ningún servidor!")));
        });
    }
}
