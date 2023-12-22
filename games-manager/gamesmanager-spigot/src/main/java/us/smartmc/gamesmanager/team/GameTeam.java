package us.smartmc.gamesmanager.team;

import lombok.Getter;
import lombok.Setter;
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
  private final List<GamePlayer> players;

  public GameTeam(TeamColor color) {
    this.players = new ArrayList<>();
    this.color = color;
  }

  public void joinPlayer(GamePlayer player) {
    if (this.hasPlayer(player.getUuid())) return;

    final PlayerJoinTeamEvent event = PlayerJoinTeamEvent.triggerEvent(player, this);
    if (event.isCancelled()) return;

    this.players.add(player);
    player.setTeam(this);
  }

  /* Unmodifiable */
  public List<GamePlayer> getPlayers() {
    return Collections.unmodifiableList(this.players);
  }

  public void leavePlayer(GamePlayer player) {
    if (!this.hasPlayer(player.getUuid())) return;

    final PlayerLeaveTeamEvent event = PlayerLeaveTeamEvent.triggerEvent(player, this);
    if (event.isCancelled()) return;

    this.players.removeIf(p -> p.getUuid().equals(player.getUuid()));
    player.setTeam(null);
  }

  public boolean hasPlayer(GamePlayer player) {
    return this.players.contains(player);
  }

  public boolean hasPlayer(UUID uuid) {
    return this.players.stream().anyMatch(p -> p.getUuid().equals(uuid));
  }
}
