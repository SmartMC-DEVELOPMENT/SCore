package us.smartmc.gamesmanager.team.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import us.smartmc.gamesmanager.manager.CancellableEvent;
import us.smartmc.gamesmanager.team.GameTeam;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerLeaveTeamEvent extends CancellableEvent {

  private static final HandlerList HANDLERS_LIST = new HandlerList();

  private final Player player;
  private final GameTeam team;

  @Override
  public HandlerList getHandlers() {
    return PlayerLeaveTeamEvent.HANDLERS_LIST;
  }

  public static PlayerLeaveTeamEvent triggerEvent(Player player, GameTeam team) {
    final PlayerLeaveTeamEvent event = new PlayerLeaveTeamEvent(player, team);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
}
