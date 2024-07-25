package us.smartmc.smartcore.proxy.command.admin;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.instance.CoreCommand;

public class BroadcastCommand extends CoreCommand {


    public BroadcastCommand() {
        super("broadcast", "smartmc.admin", "bc", "alert", "anuncio", "anunciar");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("smartmc.admin")) return;
        if (args.length == 0) {
            sendStringMessage("proxy_main", sender, "broadcast_incorrect_usage");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String word : args) {
            message.append(word).append(" ");
        }
        sendBroadcast(message.toString());
    }

    private void sendBroadcast(String message) {
        message = message.replace("&", "§");
        String formattedMessage = "<gradient:#00c0ff:#94E3FF><bold>SmartMC</gradient> &r<gray>» &r" + message;
        BaseComponent component = ChatUtil.parse(formattedMessage);
        for (ProxiedPlayer player : BungeeCordPluginsAPI.proxy.getPlayers()) {
            player.sendMessage(component);
        }
    }
}
