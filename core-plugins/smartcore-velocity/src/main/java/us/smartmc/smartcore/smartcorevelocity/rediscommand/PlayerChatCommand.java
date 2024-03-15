package us.smartmc.smartcore.smartcorevelocity.rediscommand;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

public class PlayerChatCommand extends RedisPubSubListener {

    public PlayerChatCommand() {
        super("PlayerChat");
    }

    @Override
    public void onMessage(String message) {
        String[] args = message.split("\n");
        String userName = args[0];
        String command = args[1];

        Player player = VelocityPluginsAPI.proxy.getPlayer(userName).get();
        VelocityPluginsAPI.proxy.getCommandManager().executeAsync(player, command);
    }
}
