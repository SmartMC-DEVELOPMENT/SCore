import us.smartmc.gamescore.instance.game.team.ColorGameTeam;
import us.smartmc.gamescore.instance.game.team.ColorGameTeamColor;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.GameTeamsManager;

public class TestGameTeams {

    public static void main(String[] args) {
        GameTeamsManager manager = MapManager.getManager(GameTeamsManager.class);
        if (manager == null) return;
        GameTeam gameTeam = new GameTeam("yellow");
        manager.put(gameTeam.getName(), gameTeam);

        GameTeam managerTeam = manager.get("yellow");
        System.out.println("managerTeam = " + managerTeam.getName());
    }

}
