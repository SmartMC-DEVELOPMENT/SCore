package us.smartmc.smartcore.smartcorevelocity.command.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

import java.net.InetSocketAddress;

public class ServerHandlerCommand extends CoreCommand {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    public ServerHandlerCommand() {
        super("serverHandler", "smartmc.admin.serverhandler");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("smartmc.admin")) return;
        if (args.length == 0) {
            sender.sendMessage(VelocityChatUtil.parse("&cUso correcto: /serverHandler register, unregister <arguments>"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "register": {
                if (args.length >= 3) {
                    String name = args[1];
                    String[] addressArguments = args[2].split(":");
                    InetSocketAddress address = new InetSocketAddress(addressArguments[0], Integer.parseInt(addressArguments[1]));
                    register(name, address);
                    sender.sendMessage(VelocityChatUtil.parse("&aServidor agreado y registrado correctamente."));
                }
                break;
            }
            case "unregister": {
                if (args.length >= 2) {
                    String name = args[1];
                    unregister(name);
                    sender.sendMessage(VelocityChatUtil.parse("&aServidor removido correctamente."));
                }
            }
        }
    }

    public void saveConfig() {

    }

    public void unregister(String name) {
        VelocityPluginsAPI.proxy.unregisterServer(VelocityPluginsAPI.proxy.getServer(name).get().getServerInfo());
    }

    public void register(String name, InetSocketAddress address) {
        ServerInfo serverInfo = new ServerInfo(name, address);
        VelocityPluginsAPI.proxy.registerServer(serverInfo);
    }

}
