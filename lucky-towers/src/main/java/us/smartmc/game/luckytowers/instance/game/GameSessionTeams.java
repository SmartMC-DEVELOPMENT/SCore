package us.smartmc.game.luckytowers.instance.game;

import us.smartmc.game.luckytowers.instance.player.GamePlayer;

import java.util.UUID;
import java.util.function.Consumer;

public class GameSessionTeams {

    private final GameSession session;

    private final GameTeam[] gameTeams;

    public GameSessionTeams(GameSession session) {
        this.session = session;
        this.gameTeams = getFilledTeamsArray();
    }

    public int getFreeSlots() {
        int slots = 0;
        int teamCapacity = session.getMap().getTemplate().getMaxTeamCapacity();
        for (GameTeam team : gameTeams) {
            int size = team.getPlayersSize();
            int min = Math.min(teamCapacity, size);
            int max = Math.max(teamCapacity, size);
            int difference = max - min;
            slots += difference;
        }
        return slots - 1;
    }

    public int getTeamsWithPlayersSize() {
        int count = 0;
        for (GameTeam team : gameTeams) {
            if (team.isEmpty()) continue;
            count++;
        }
        return count;
    }

    public void forEachTeam(Consumer<GameTeam> consumer) {
        for (GameTeam team : gameTeams) {
            consumer.accept(team);
        }
    }

    public void setTeam(GamePlayer player, GameTeamColor teamColor) {
        GameTeam teamToAssign = null;
        for (GameTeam team : gameTeams) {
            if (!team.getColor().equals(teamColor)) continue;
            teamToAssign = team;
            break;
        }
        clearTeams(player);
        if (teamToAssign == null) {
            System.out.println("Couldn't find a valid team for color " + teamColor.name() + " for " + player.getBukkitPlayer().getName());
            return;
        }
        teamToAssign.addPlayer(player.getUuid());
    }

    public GameTeam assignNextEmptyTeam(GamePlayer player) {
        GameTeam team = getNextEmptyTeam();
        team.addPlayer(player.getUuid());
        return team;
    }

    public void clearTeams(GamePlayer player) {
        UUID id = player.getUuid();
        for (GameTeam team : gameTeams) {
            team.removePlayer(id);
        }
    }

    public GameTeam getNextEmptyTeam() {
        int freeIndex = getNextFreeIndex();
        return gameTeams[freeIndex];
    }

    private int getNextFreeIndex() {
        int index = 0;
        for (GameTeam team : gameTeams) {
            if (team.isEmpty()) return index;
            index++;
        }
        return -1;
    }

    private GameTeam[] getFilledTeamsArray() {
        GameTeam[] teams = new GameTeam[session.getMap().getSpawnLocations().size()];
        int index = 0;
        for (GameTeamColor color : GameTeamColor.values()) {
            GameTeam team = new GameTeam(color, session.getMap().getSpawnLocations().get(index));
            if (index >= teams.length) break;
            teams[index] = team;
            index++;
        }
        return teams;
    }

}
