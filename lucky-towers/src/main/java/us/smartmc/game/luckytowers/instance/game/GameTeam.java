package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameTeam {

    @Getter
    private final GameTeamColor color;
    @Getter
    private final Set<UUID> players = new HashSet<>();

    private final Location spawnAssigned;

    public GameTeam(GameTeamColor color, Location spawnAssigned) {
        this.color = color;
        this.spawnAssigned = spawnAssigned;
    }

    public Location getSpawnAssigned(World world, int additionXLocation) {
        if (spawnAssigned.getWorld() == null)
            spawnAssigned.setWorld(world);
        return spawnAssigned.add(additionXLocation, 0, 0);
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
