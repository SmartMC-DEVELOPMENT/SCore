package us.smartmc.arenapvp.arenapvp.instance.player;

import us.smartmc.arenapvp.arenapvp.ArenaPvP;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.data.IOfflineGamePlayerData;

import java.util.UUID;

public class ArenaPlayer extends GamePlayer {

    private static final ArenaPvP plugin = ArenaPvP.getPlugin(ArenaPvP.class);

    private final ArenaPlayerData playerData;

    public ArenaPlayer(UUID uuid) {
        super(plugin.getGamePlayerManager(), uuid);
        playerData = new ArenaPlayerData(uuid);
    }

    @Override
    public void load() {
        playerData.load();
    }

    @Override
    public void unload() {
        playerData.save();
    }

    @Override
    public IOfflineGamePlayerData getData() {
        return playerData;
    }
}
