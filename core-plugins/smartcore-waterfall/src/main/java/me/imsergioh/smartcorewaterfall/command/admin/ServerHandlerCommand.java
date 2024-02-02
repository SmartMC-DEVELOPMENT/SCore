package me.imsergioh.smartcorewaterfall.command.admin;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class ServerHandlerCommand extends CoreCommand {

    private static final SmartCoreWaterfall plugin = SmartCoreWaterfall.getPlugin();
    private static final ProxyServer proxy = plugin.getProxy();

    public ServerHandlerCommand() {
        super("serverHandler", "smartmc.admin.serverhandler");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtil.parse("&cUso correcto: /serverHandler register, unregister <arguments>"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "register" -> {
                if (args.length >= 3) {
                    String name = args[1];
                    String[] addressArguments = args[2].split(":");
                    InetSocketAddress address = new InetSocketAddress(addressArguments[0], Integer.parseInt(addressArguments[1]));
                    String motd = "A registered server by ServerHandler";
                    boolean restricted = false;
                    register(name, address, motd, restricted);
                    sender.sendMessage(ChatUtil.parse("&aServidor agreado y registrado correctamente."));
                }
            }

            case "unregister" -> {
                if (args.length >= 2) {
                    String name = args[1];
                    unregister(name);
                    sender.sendMessage(ChatUtil.parse("&aServidor removido correctamente."));
                }
            }
        }
    }

    public void saveConfig() {

    }

    public void unregister(String name) {
        proxy.getConfig().removeServerNamed(name);
    }

    public void register(String name, InetSocketAddress address, String motd, boolean restricted) {
        ServerInfo serverInfo = proxy.constructServerInfo(name, address, motd, restricted);
        proxy.getConfig().addServer(serverInfo);
    }

}
