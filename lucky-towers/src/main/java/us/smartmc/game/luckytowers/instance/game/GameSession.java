package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class GameSession implements IGameSession {

    private final UUID id;

    private final GameMap map;

    private final Set<GamePlayer> players = new HashSet<>();
    private GameSessionTeams teams;

    private GameSession(UUID id, GameMap map) {
        this.id = id;
        this.map = map;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public boolean canStart() {
        return false;
    }

    @Override
    public boolean canEnd() {
        return false;
    }

    @Override
    public void joinPlayer(GamePlayer player) {
        GameTeam team = teams.assignNextEmptyTeam(player);
        player.onlinePlayer(p -> p.teleport(team.getSpawnAssigned()));
    }

    @Override
    public void quitPlayer(GamePlayer player) {
        teams.clearTeams(player);
    }

    @Override
    public void deathPlayer(GamePlayer player) {

    }

    // Only create instance at the first get method (Ram issues)
    public GameSessionTeams getTeams() {
        if (teams != null) return teams;
        teams = new GameSessionTeams(this);
        return teams;
    }
}
