import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.GenericGameTeamsManager;

public class TestGameTeams {

    public static void main(String[] args) {
        GenericGameTeamsManager manager = MapManager.getManager(GenericGameTeamsManager.class);
        if (manager == null) return;
        GameTeam gameTeam = new GameTeam("yellow");
        manager.put(gameTeam.getName(), gameTeam);

        GameTeam managerTeam = manager.get("yellow");
        System.out.println("managerTeam = " + managerTeam.getName());
    }

}
