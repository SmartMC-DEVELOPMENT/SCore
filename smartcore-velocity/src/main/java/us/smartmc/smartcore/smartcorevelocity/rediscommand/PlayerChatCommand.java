package us.smartmc.smartcore.smartcorevelocity.rediscommand;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

import java.util.Optional;

public class PlayerChatCommand extends RedisPubSubListener {

    public PlayerChatCommand() {
        super("PlayerChat");
    }

    @Override
    public void onMessage(String message) {
        String[] args = message.split("\n");
        String userName = args[0];
        String command = args[1];

        Optional<Player> optional = VelocityPluginsAPI.proxy.getPlayer(userName);
        if (optional.isEmpty()) return;
        Player player = optional.get();
        VelocityPluginsAPI.proxy.getCommandManager().executeAsync(player, command);
    }
}
