package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.core.pluginsapi.util.ChatUtil;

import java.util.Collections;

public class BroadcastCommand extends CoreCommand {


    public BroadcastCommand() {
        super("broadcast", "smartmc.admin", "bc", "alert", "anuncio", "anunciar");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendStringMessage("proxy_main", sender, "broadcast_incorrect_usage");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String word : args) {
            message.append(word).append(" ");
        }

        sendBroadcast(message.toString().trim().replaceAll("&", "§"));
    }

    private void sendBroadcast(String message) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage("§b§lSmart§f§lMC §8» §f"+message);
        }
    }
}
