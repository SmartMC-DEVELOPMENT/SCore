package us.smartmc.gamesmanager.gamesmanagerspigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class GamesManagerAPI extends JavaPlugin implements IGamesManagerAPI {

    private final GamesManagerAPI plugin;

    public GamesManagerAPI() {
        this.plugin = this;
    }

}
