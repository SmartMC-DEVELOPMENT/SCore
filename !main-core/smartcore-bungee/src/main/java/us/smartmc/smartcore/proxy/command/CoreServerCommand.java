package us.smartmc.smartcore.proxy.command;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import us.smartmc.smartcore.proxy.instance.CoreCommand;

public class CoreServerCommand extends CoreCommand {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    public CoreServerCommand() {
        super("server");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(ChatUtil.parse("&bSmartCore plugin v" +
                    plugin.getDescription().getVersion() + " by ImSergioh!"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 0) {
            sender.sendMessage(ChatUtil.parse("&cTienes que especificar a que server te quieres conectar"));
            return;
        }

        String serverTargetName = args[0];
        ServerInfo server = BungeeCordPluginsAPI.getPlugin().getProxy().getServers().get(serverTargetName);

        if (server == null) {
            player.sendMessage(ChatUtil.parse("&cNo se ha encontrado ese servidor registrado a tu proxy actual"));
            return;
        }

        player.sendMessage(ChatUtil.parse("&aConectando a &e" + server.getName() + "&a..."));
        player.connect(server);
    }
}
