package us.smartmc.gamesmanager.team;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.team.event.PlayerJoinTeamEvent;
import us.smartmc.gamesmanager.team.event.PlayerLeaveTeamEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameTeam {

  @Getter
  @Setter
  private TeamColor color;
  private final List<Player> players;

  public GameTeam(TeamColor color) {
    this.players = new ArrayList<>();
    this.color = color;
  }

  public void joinPlayer(Player player) {
    if (this.hasPlayer(player.getUniqueId())) return;

    final PlayerJoinTeamEvent event = PlayerJoinTeamEvent.triggerEvent(player, this);
    if (event.isCancelled()) return;

    this.players.add(player);
    GamePlayer.get(player).setGameTeam(this);
  }

  /* Unmodifiable */
  public List<Player> getPlayers() {
    return Collections.unmodifiableList(this.players);
  }

  public void leavePlayer(Player player) {
    if (!this.hasPlayer(player.getUniqueId())) return;

    final PlayerLeaveTeamEvent event = PlayerLeaveTeamEvent.triggerEvent(player, this);
    if (event.isCancelled()) return;

    this.players.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()));
    GamePlayer.get(player).setGameTeam(null);
  }

  public boolean hasPlayer(Player player) {
    return this.players.contains(player);
  }

  public boolean hasPlayer(UUID uuid) {
    return this.players.stream().anyMatch(p -> p.getUniqueId().equals(uuid));
  }
}
