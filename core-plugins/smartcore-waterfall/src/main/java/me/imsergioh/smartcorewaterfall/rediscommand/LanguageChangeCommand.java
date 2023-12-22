package me.imsergioh.smartcorewaterfall.rediscommand;

import us.smartmc.core.pluginsapi.instance.handler.RedisPubSubListener;
import us.smartmc.core.pluginsapi.language.Language;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.listener.TabHandlerListeners;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class LanguageChangeCommand extends RedisPubSubListener {

    public LanguageChangeCommand() {
        super("lang:change");
    }

    @Override
    public void onMessage(String message) {
        String[] args = message.split(" ");
        UUID uuid = UUID.fromString(args[0]);
        Language language = Language.valueOf(args[1]);

        ProxiedPlayer player = SmartCoreWaterfall.getPlugin().getProxy().getPlayer(uuid);
        if (player == null) return;
        if (!player.isConnected()) return;
        PlayerLanguages.set(player, language);
        TabHandlerListeners.sendTab(player);
    }
}
