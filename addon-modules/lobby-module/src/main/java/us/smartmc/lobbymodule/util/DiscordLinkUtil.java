package us.smartmc.lobbymodule.util;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DiscordLinkUtil {

    private static final String LINK_CODE_TEMPLATE = "xxxxxx";
    private final static Map<UUID, String> linkDiscordCodes = new HashMap<>();

    public static String getOrGenerateLinkCode(Player player) {
        if (linkDiscordCodes.containsKey(player.getUniqueId())) return linkDiscordCodes.get(player.getUniqueId());
        String code = generateRandomLinkCode();
        RedisConnection.mainConnection.getResource().set(getKey(code), player.getUniqueId().toString());
        linkDiscordCodes.put(player.getUniqueId(), code);
        return code;
    }

    public static void clearLinkDiscordCode(Player player) {
        String code = linkDiscordCodes.get(player.getUniqueId());
        if (code == null) return;
        linkDiscordCodes.remove(player.getUniqueId());
        RedisConnection.mainConnection.getResource().del(getKey(code));
    }

    public static String getKey(String code) {
        return "linkDiscord." + code;
    }

    private static String generateRandomLinkCode() {
        String code = LINK_CODE_TEMPLATE;
        while (code.contains("x")) {
            code = code.replaceFirst("x", String.valueOf(new Random().nextInt(9)));
        }
        return code;
    }

}
