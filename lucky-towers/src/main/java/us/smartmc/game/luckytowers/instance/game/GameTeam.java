package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class GameTeam {

    private final GameTeamColor color;
    private final Set<UUID> players = new HashSet<>();
    private final Location spawnAssigned;

    public GameTeam(GameTeamColor color, Location spawnAssigned) {
        this.color = color;
        this.spawnAssigned = spawnAssigned;
    }

    public int getPlayersSize() {
        return players.size();
    }

    public void addPlayer(UUID id) {
        players.add(id);
    }

    public void removePlayer(UUID id) {
        players.remove(id);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

}
