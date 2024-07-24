package us.smartmc.smartcore.proxy.rediscommand;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.instance.PlayerLanguages;

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

        ProxiedPlayer player = BungeeCordPluginsAPI.proxy.getPlayer(uuid);
        PlayerLanguages.set(player, language);
    }
}