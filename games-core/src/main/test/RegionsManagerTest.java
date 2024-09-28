import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.manager.RegionsManager;

public class RegionsManagerTest {

    public static void main(String[] args) throws Exception {
        RegionsManager regionsManager = RegionsManager.getManager(RegionsManager.class);
        if (regionsManager == null) throw new Exception("No valid manager found");
        CuboidRegion parentRegion = regionsManager.register("test");

        parentRegion.getConfig().registerSubRegion(new CuboidRegion(""));

        System.out.println("Region: " + parentRegion);
    }

}
