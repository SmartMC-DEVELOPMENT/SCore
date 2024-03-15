package us.smartmc.smartcore.smartcorevelocity.rediscommand;

import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;
import us.smartmc.smartcore.smartcorevelocity.listener.TabHandlerListeners;

import java.util.Optional;
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

        Optional<Player> oPlayer = VelocityPluginsAPI.proxy.getPlayer(uuid);
        if (oPlayer.isEmpty()) return;
        Player player = oPlayer.get();
        PlayerLanguages.set(player, language);
        TabHandlerListeners.sendTab(player);
    }
}
