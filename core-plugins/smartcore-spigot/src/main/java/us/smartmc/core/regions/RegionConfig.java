package us.smartmc.core.regions;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import us.smartmc.core.SmartCore;

import java.io.File;
import java.util.List;

@Getter
public class RegionConfig extends SpigotYmlConfig implements ICuboidRegionManager {

    private static final SmartCore plugin = SmartCore.getPlugin();

    private final String name;
    private Cuboid cuboidRegion;

    public RegionConfig(String name) {
        super(new File(CuboidManager.getRegionsParentDirectory(), name + ".yml"));
        this.name = name;
        if (contains("loc1") && contains("loc2")) {
            cuboidRegion = new Cuboid(LocationSerializer.toLocation(getString("loc1")),
                    LocationSerializer.toLocation(getString("loc2")));
        }

    }

    @Override
    public void setCuboidRegion(Cuboid cuboidRegion) {
        this.cuboidRegion = cuboidRegion;
        set("loc1", LocationSerializer.toString(cuboidRegion.getLoc1()));
        set("loc2", LocationSerializer.toString(cuboidRegion.getLoc2()));
    }

    @Override
    public void addRegionMeta(String metaString) {
        List<String> list = getStringList("meta");
        list.add(metaString);
        set("meta", list);
    }

    @Override
    public int getPriority() {
        if (contains("priority")) return getInt("priority");
        return 0;
    }

    @Override
    public void setPriority(int priority) {
        set("priority", priority);
    }

    @Override
    public List<String> getRegionMetas() {
        return getStringList("meta");
    }
}