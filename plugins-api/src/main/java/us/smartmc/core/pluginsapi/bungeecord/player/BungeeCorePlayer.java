package us.smartmc.core.pluginsapi.bungeecord.player;

import us.smartmc.core.pluginsapi.data.player.OfflineCorePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeCorePlayer extends OfflineCorePlayer<ProxiedPlayer> {

    protected final ProxiedPlayer player;
    protected BungeeCorePlayer(UUID uuid) {
        super(uuid);
        player = get();
    }

    public String getIP() {
        return get().getPendingConnection().getAddress().getAddress().toString();
    }

    @Override
    public void load() {
        super.load();
        offlineDataDocument.put("name", player.getName());
        offlineDataDocument.put("query_name", player.getName().toLowerCase());
    }

    @Override
    public ProxiedPlayer get() {
        return BungeeCordPluginsAPI.getServer().getPlayer(uuid);
    }
}
