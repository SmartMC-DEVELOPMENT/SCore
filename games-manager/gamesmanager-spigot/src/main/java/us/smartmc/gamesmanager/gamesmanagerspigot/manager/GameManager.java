package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.IGameInstance;

import java.util.HashMap;

public class GameManager<T extends IGameInstance> {

    private final HashMap<String, T> games = new HashMap<>();

    public void register(T game) {
        games.put(game.getName(), game);
        game.load();
    }

    public void unregister(String name) {
        get(name).unload();
        games.remove(name);
    }

    public T get(String name) {
        return games.get(name);
    }
}
