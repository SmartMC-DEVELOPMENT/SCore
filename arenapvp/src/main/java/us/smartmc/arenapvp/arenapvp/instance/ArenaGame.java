package us.smartmc.arenapvp.arenapvp.instance;

import us.smartmc.arenapvp.arenapvp.ArenaPvP;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

public class ArenaGame extends GameInstance {

    public ArenaGame(String name) {
        super(ArenaPvP.getPlugin().getGameManager(), name);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void joinPlayer(GamePlayer player) {

    }

    @Override
    public void quitPlayer(GamePlayer player) {

    }
}
