package us.smartmc.smartcore.smartcorevelocity.manager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.backend.PlayerServerConnectionsHandler;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServersHandler {

    public static void connectToHubByHubRules(Player player) {
        String at = getAt(player);
        List<String> lobbies = getLobbyServers(at);
        String randomLobby = lobbies.get(new Random().nextInt(lobbies.size()));

        RegisteredServer server = VelocityPluginsAPI.proxy.getServer(
                randomLobby
        ).get();

        PlayerServerConnectionsHandler.get(player).sendConnectionQueue(server);
    }

    private static String getAt(Player player) {
        Document hubRules = SmartCoreVelocity.getConfig().get("hubRules", Document.class);
        String serverName = player.getCurrentServer().get().getServerInfo().getName();
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
        for (RegisteredServer server : VelocityPluginsAPI.proxy.getAllServers()) {
            String serverName = server.getServerInfo().getName();
            if (serverName.startsWith(path)) {
                list.add(serverName);
            }
        }
        return list;
    }

    public static boolean isAtLobbyServer(Player player) {
        return player.getCurrentServer().get().getServerInfo().getName().startsWith("lobby");
    }

    public static boolean isAtBlockedServer(Player player) {
        return getBlockedServers().contains(player.getCurrentServer().get().getServerInfo().getName());
    }

    public static List<String> getBlockedServers() {
        return SmartCoreVelocity.getConfig().getList("authServers", String.class);
    }
}
