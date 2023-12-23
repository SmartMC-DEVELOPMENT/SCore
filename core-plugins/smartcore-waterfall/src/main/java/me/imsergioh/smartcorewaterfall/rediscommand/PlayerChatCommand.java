package me.imsergioh.smartcorewaterfall.rediscommand;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerChatCommand extends RedisPubSubListener {

    public PlayerChatCommand() {
        super("PlayerChat");
    }

    @Override
    public void onMessage(String message) {
        String[] args = message.split("\n");
        String userName = args[0];
        String command = args[1];

        ProxiedPlayer player = SmartCoreWaterfall.getPlugin().getProxy().getPlayer(userName);
        SmartCoreWaterfall.getPlugin().getProxy().getPluginManager().dispatchCommand(player, command);
    }
}
