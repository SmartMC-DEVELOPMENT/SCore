package us.smartmc.smartcore.proxy.manager;

import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServersHandler {

    public static void connectToHubByHubRules(ProxiedPlayer player) {
        String at = getAt(player);
        List<String> lobbies = getLobbyServers(at);
        String randomLobby = lobbies.get(new Random().nextInt(lobbies.size()));

        ServerInfo server = BungeeCordPluginsAPI.proxy.getServerInfo(
                randomLobby
        );

        player.connect(server);
    }

    private static String getAt(ProxiedPlayer player) {
        Document hubRules = SmartCoreBungeeCord.getConfig().get("hubRules", Document.class);
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
        for (ServerInfo server : BungeeCordPluginsAPI.proxy.getServers().values()) {
            String serverName = server.getName();
            if (serverName.startsWith(path)) {
                list.add(serverName);
            }
        }
        return list;
    }

    public static boolean isAtLobbyServer(ProxiedPlayer player) {
        return player.getServer().getInfo().getName().startsWith("lobby");
    }

    public static boolean isAtBlockedServer(ProxiedPlayer player) {
        return getBlockedServers().contains(player.getServer().getInfo().getName());
    }

    public static List<String> getBlockedServers() {
        return SmartCoreBungeeCord.getConfig().getList("authServers", String.class);
    }
}
