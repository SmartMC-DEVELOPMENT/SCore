package us.smartmc.gamesmanager.game.map;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.gamesmanager.game.teams.PlayerTeam;
import us.smartmc.gamesmanager.manager.GameMapManager;
import us.smartmc.gamesmanager.util.WorldUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private static final String MIN_PLAYERS_PATH = "min_players";
    private static final String MAX_PLAYERS_PATH = "max_players";
    private static final String MAX_TEAM_SIZE = "max_team_size";
    private static final String MAX_ARENA_TIME = "max_time";

    @Getter
    protected final String name;

    protected final FilePluginConfig config;

    // LOAD
    public GameMap(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(GameMapManager.getMapsDir(), name + ".json")).load();
        registerConfigDefault(MIN_PLAYERS_PATH, 2);
        registerConfigDefault(MAX_PLAYERS_PATH, 12);
        config.registerDefault(MAX_TEAM_SIZE, 2);
        registerConfigDefault(MAX_ARENA_TIME, 15 * 60 * 20);
        config.save();
    }

    // CREATE
    public GameMap(String name, World world) {
        this(name);
        WorldUtils.copyWorldToDir(world.getName(), getMapDir().getAbsolutePath());
    }

    public int getMaxPlayerSizePerTeam() {
        return getMaxPlayers() / getMaxTeamsSize();
    }

    public void addTeamLocation(PlayerTeam team, Location location) {
        String path = locationsPath(team);
        List<String> locations = config.getList(path, String.class, new ArrayList<>());
        locations.add(toString(location, true));
        config.put(path, locations);
        save();
    }

    public List<Location> getTeamLocations(World world, PlayerTeam team) {
        String path = locationsPath(team);
        List<Location> locations = new ArrayList<>();
        List<String> list = config.getList(path, String.class);

        for (String stringLocation : list) {
            locations.add(fromString(world, stringLocation, true));
        }
        return locations;
    }

    public void save() {
        config.save();
    }

    public File getMapDir() {
        return new File(GameMapManager.getMapsDir(), name);
    }

    public void setMinPlayers(int count) {
        config.put(MIN_PLAYERS_PATH, count);
    }


    public int getMinPlayers() {
        return config.get(MIN_PLAYERS_PATH, Integer.class);
    }

    public void setMaxPlayers(int count) {
        config.put(MAX_PLAYERS_PATH, count);
    }

    public int getMaxPlayers() {
        return config.get(MAX_PLAYERS_PATH, Integer.class);
    }

    public void setMaxTeamsSize(int amount) {
        config.put(MAX_TEAM_SIZE, amount);
    }

    public int getMaxTeamsSize() {
        return config.getInteger(MAX_TEAM_SIZE);
    }

    public void setMaxArenaTime(int time) {
        config.put(MAX_ARENA_TIME, time);
    }

    public int getMaxArenaTime() {
        return config.getInteger(MAX_ARENA_TIME);
    }

    public static GameMap get(String name) {
        return GameMapManager.get(name);
    }

    private String toString(Location location, boolean withYawAndPitch) {
        StringBuilder str = new StringBuilder();
        str.append(location.getX() + ' ');
        str.append(location.getY() + ' ');
        str.append(location.getZ() + ' ');
        if (withYawAndPitch) {
            str.append(location.getYaw() + ' ');
            str.append(location.getPitch());
        }
        return str.toString();
    }

    private Location fromString(World world, String stringLoc, boolean withYawAndPitch) {
        String[] args = stringLoc.split(" ");
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        float yaw = Float.parseFloat(args[3]);
        float pitch = Float.parseFloat(args[4]);
        if (withYawAndPitch) {
            return new Location(world, x, y, z, yaw, pitch);
        }
        return new Location(world, x, y, z);
    }

    public void registerConfigDefault(String path, Object value) {
        if (config.containsKey(path)) return;
        config.put(path, value);
    }

    public static String locationsPath(PlayerTeam team) {
        return "locations_" + team.name();
    }
}
