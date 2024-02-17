package us.smartmc.gamesmanager.gamesmanagerspigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

public abstract class GamesManagerAPI<GameType extends GameInstance, PlayerType extends GamePlayer> extends JavaPlugin implements IGamesManagerAPI<GameType, PlayerType> {
}
