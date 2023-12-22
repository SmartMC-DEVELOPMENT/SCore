package me.imsergioh.smartcorewaterfall.rediscommand;

import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.listener.TabHandlerListeners;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.core.pluginsapi.instance.handler.RedisPubSubListener;
import us.smartmc.core.pluginsapi.language.Language;

import java.util.UUID;

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
