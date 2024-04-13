package us.smartmc.moderation.staffmodebungee.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.moderation.staffmodebungee.instance.StaffPlayer;

import java.util.HashMap;
import java.util.UUID;

public class StaffPlayerManager {

    private final HashMap<UUID, StaffPlayer> players = new HashMap<>();

    public void register(ProxiedPlayer player) {
        players.put(player.getUniqueId(), new StaffPlayer(player));
        get(player).load();
    }

    public void unregister(ProxiedPlayer player) {
        StaffPlayer staffPlayer = get(player);
        if (staffPlayer == null) return;
        staffPlayer.unload();
        players.remove(player.getUniqueId());
    }

    public StaffPlayer get(ProxiedPlayer player) {
        return players.get(player.getUniqueId());
    }

}
