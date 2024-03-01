package me.imsergioh.smartcorewaterfall.manager;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServersHandler {

    public static void connectToHubByHubRules(ProxiedPlayer player) {
        String at = getAt(player);
        List<String> lobbies = getLobbyServers(at);
        String randomLobby = lobbies.get(new Random().nextInt(lobbies.size()));

        String serverName = SmartCoreWaterfall.getPlugin().getProxy().getServerInfo(
                randomLobby
        ).getName();
        try {
            RedisConnection.mainConnection.getResource().publish("connectServer", player.getName() + " " + serverName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static String getAt(ProxiedPlayer player) {
        Document hubRules = SmartCoreWaterfall.getConfig().get("hubRules", Document.class);
        String serverName = player.getServer().getInfo().getName();
        for (String hubRuleKey : hubRules.keySet()) {
            String key = hubRuleKey.replace("*", "");
            String value = hubRules.getString(hubRuleKey).replace("*", "");
            if (serverName.startsWith(key) && !serverName.startsWith(value)) {
                return hubRules.getString(hubRuleKey).replace("*", "");
            }
        }
        // If not found gets first rule value
        return hubRules.getString(new ArrayList<>(hubRules.keySet()).get(0)).replace("*", "");
    }

    public static List<String> getLobbyServers(String path) {
        List<String> list = new ArrayList<>();

        for (String serverName : SmartCoreWaterfall.getPlugin().getProxy().getServersCopy().keySet()) {
            if (serverName.startsWith(path)) {
                list.add(serverName);
            }
        }
        return list;
    }

    public static boolean isAtLobbyServer(ProxiedPlayer player) {
        return player.getServer().getInfo().getName().startsWith("main-lobby-");
    }

    public static boolean isAtBlockedServer(ProxiedPlayer player) {
        return getBlockedServers().contains(player.getServer().getInfo().getName());
    }

    public static List<String> getBlockedServers() {
        return SmartCoreWaterfall.getConfig().getList("authServers", String.class);
    }
}
