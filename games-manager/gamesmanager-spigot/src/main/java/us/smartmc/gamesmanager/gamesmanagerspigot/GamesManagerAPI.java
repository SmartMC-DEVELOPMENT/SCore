package us.smartmc.gamesmanager.gamesmanagerspigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GamesManagerAPI extends JavaPlugin implements IGamesManagerAPI {

    @Getter
    private final GamesManagerAPI plugin;

    public GamesManagerAPI() {
        this.plugin = this;
    }

}
