package us.smartmc.smartcore.proxy.command.admin;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import us.smartmc.smartcore.proxy.instance.CoreCommand;

import java.net.InetSocketAddress;

public class ServerHandlerCommand extends CoreCommand {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    public ServerHandlerCommand() {
        super("serverHandler", "smartmc.admin.serverhandler");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("smartmc.admin")) return;
        if (args.length == 0) {
            sender.sendMessage(ChatUtil.parse("&cUso correcto: /serverHandler register, unregister <arguments>"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "register": {
                if (args.length >= 3) {
                    String name = args[1];
                    String[] addressArguments = args[2].split(":");
                    InetSocketAddress address = new InetSocketAddress(addressArguments[0], Integer.parseInt(addressArguments[1]));
                    register(name, address);
                    sender.sendMessage(ChatUtil.parse("&aServidor agreado y registrado correctamente."));
                }
                break;
            }
            case "unregister": {
                if (args.length >= 2) {
                    String name = args[1];
                    unregister(name);
                    sender.sendMessage(ChatUtil.parse("&aServidor removido correctamente."));
                }
            }
        }
    }

    public void unregister(String name) {
        BungeeCordPluginsAPI.proxy.getServers().remove(name);
    }

    public void register(String name, InetSocketAddress address) {
        ServerInfo info = ProxyServer.getInstance().constructServerInfo(name, address, "ServerHandler registered server", false);
        ProxyServer.getInstance().getServers().put(name, info);
    }

}
