package us.smartmc.gamesmanager.game;

import us.smartmc.gamesmanager.game.map.GameMap;

import java.util.List;

@SuppressWarnings("unused")
public interface IGamePreset {

    void unload();

    List<String> getMapWhitelist();
    void addMapToWhitelist(GameMap map);
    void removeMapFromWhitelist(GameMap map);

    boolean isInDevelopment();


    String getName();

}
