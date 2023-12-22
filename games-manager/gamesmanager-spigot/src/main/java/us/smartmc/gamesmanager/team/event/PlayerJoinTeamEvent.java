package us.smartmc.gamesmanager.team.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import us.smartmc.gamesmanager.manager.CancellableEvent;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.team.GameTeam;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerJoinTeamEvent extends CancellableEvent {

  private static final HandlerList HANDLERS_LIST = new HandlerList();

  private final GamePlayer player;
  private final GameTeam team;

  @Override
  public HandlerList getHandlers() {
    return PlayerJoinTeamEvent.HANDLERS_LIST;
  }

  public static PlayerJoinTeamEvent triggerEvent(GamePlayer player, GameTeam team) {
    final PlayerJoinTeamEvent event = new PlayerJoinTeamEvent(player, team);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
}
