package us.smartmc.smartcore.smartcorevelocity.command.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;

public class BroadcastCommand extends CoreCommand {


    public BroadcastCommand() {
        super("broadcast", "smartmc.admin", "bc", "alert", "anuncio", "anunciar");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("smartmc.admin")) return;
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
        for (Player player : VelocityPluginsAPI.proxy.getAllPlayers()) {
            player.sendMessage(Component.text("§b§lSmart§f§lMC §8» §a" + message));
        }
    }
}
