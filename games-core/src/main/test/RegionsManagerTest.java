import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.RegionsManager;

public class RegionsManagerTest {

    public static void main(String[] args) throws Exception {
        RegionsManager regionsManager = MapManager.getManager(RegionsManager.class);
        if (regionsManager == null) throw new Exception("No valid manager found");

        Cuboid
        CuboidRegion parentRegion = new CuboidRegion("test");

        parentRegion.getConfig().registerSubRegion(new CuboidRegion(parentRegion, "subregion_main"));

        CuboidRegion subregionEnCuestion = regionsManager.register("test.subregion_main");

        System.out.println("Region: " + subregionEnCuestion);
    }

}
