package us.smartmc.smartcore.smartcorevelocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.imsergioh.pluginsapi.instance.backend.PlayerServerConnectionsHandler;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

import javax.swing.text.html.Option;
import java.util.Optional;

public class CoreServerCommand extends CoreCommand {

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
        Optional<RegisteredServer> optional = VelocityPluginsAPI.proxy.getServer(serverTargetName);
        if (optional.isEmpty()) {
            player.sendMessage(Component.text(ChatUtil.color("&cNo se ha encontrado ese servidor registrado a tu proxy actual")));
            return;
        }
        RegisteredServer server = optional.get();
        player.sendMessage(Component.text(ChatUtil.color("&aConectando a &e" + server.getServerInfo().getName() + "&a...")));
        PlayerServerConnectionsHandler.get(player).sendConnectionQueue(server);
    }
}
