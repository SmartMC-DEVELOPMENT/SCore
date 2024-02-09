package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

public interface IGameMap {

    void load();

    void unload();

    void reset();

    int getMaxPlayerSize();
    int getMinPlayersSize();
    String getName();
}
