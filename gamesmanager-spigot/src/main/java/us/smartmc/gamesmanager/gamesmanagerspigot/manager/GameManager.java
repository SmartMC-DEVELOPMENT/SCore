package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameLoadedEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameRegisterEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameUnloadedEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameUnregisterEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.util.BukkitUtil;

import java.util.HashMap;

public class GameManager<T extends GameInstance> {

    private final HashMap<String, T> games = new HashMap<>();

    public void register(T game) {
        games.put(game.getName(), game);
        BukkitUtil.callEvent(new GameRegisterEvent(game));
        game.load();
        BukkitUtil.callEvent(new GameLoadedEvent(game));
    }

    public void unregister(String name) {
        GameInstance game = get(name);
        BukkitUtil.callEvent(new GameUnregisterEvent(game));
        game.unload();
        BukkitUtil.callEvent(new GameUnloadedEvent(game));
        games.remove(name);
    }

    public T get(String name) {
        return games.get(name);
    }
}
