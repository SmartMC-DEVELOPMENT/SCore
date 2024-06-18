package us.smartmc.bmotd.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import us.smartmc.bmotd.BMotdVelocity;
import us.smartmc.bmotd.util.ChatUtil;

public class maintenanceCMD implements SimpleCommand {

    private final BMotdVelocity plugin = BMotdVelocity.getPlugin();

    private void broadcastAdmins(String message) {
        for (Player player : plugin.getProxy().getAllPlayers()) {
            if (player.hasPermission("*")) {
                player.sendMessage(ChatUtil.parseToComponent(message));
            }
        }
        plugin.getProxy().sendMessage(ChatUtil.parseToComponent(message));
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (sender.hasPermission("*")) {
            boolean enabled = plugin.getMotdManager().toggleWhitelist();
            if (enabled) {
                for (Player player : BMotdVelocity.getPlugin().getProxy().getAllPlayers()) {
                    if (!plugin.getMotdManager().hasWhitelist(player.getUsername())) {
                        String message = plugin.getMotdManager().getConfig().getString("messages.not-whitelist");
                        player.disconnect(ChatUtil.parseToComponent(message));
                    }
                }
                broadcastAdmins("&fMantenimiento: &aActivado.");

            } else {
                broadcastAdmins("&fMantenimiento: &cDesactivado.");
            }
        } else {
            sender.sendMessage(ChatUtil.parseToComponent("&cPlugin BMotd vPRIVATE created by ImSergioh"));
        }
    }
}
