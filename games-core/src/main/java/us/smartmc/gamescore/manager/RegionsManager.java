package us.smartmc.gamescore.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.joml.Vector3i;
import us.smartmc.gamescore.api.GamesCoreAPI;

import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboidRegion;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.util.CuboidUtil;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class RegionsManager extends MapManager<String, CuboidRegion> {

    @Getter
    private static RegionsManager manager;

    private RegionsManager() {
        manager = this;
        System.out.println("CREATING REGIONSMANAGER");
        getRegionsDirectory().mkdirs();
        SyncUtil.async(() -> {
            for (File file : Objects.requireNonNull(getRegionsDirectory().listFiles())) {
                if (!file.getName().endsWith(".yml")) continue;
                String name = file.getName().replace(".yml", "");
                loadRegion(name);
            }
        });
    }

    public Optional<? extends CuboidRegion> getRegion(String name) {
        if (!containsKey(name)) return Optional.empty();
        return Optional.of(get(name));
    }

    public BukkitCuboidRegion createBukkitRegion(String name, BukkitCuboid cuboid) {
        return (BukkitCuboidRegion) put(name, new BukkitCuboidRegion(name, cuboid));
    }

    public BukkitCuboidRegion loadBukkitRegion(String name) {
        return (BukkitCuboidRegion) put(name, new BukkitCuboidRegion(name));
    }

    public CuboidRegion createRegion(String name, Cuboid cuboid) {
        return put(name, new CuboidRegion(name, cuboid));
    }

    public CuboidRegion loadRegion(String name) {
        if (containsKey(name)) return get(name);
        CuboidRegion region = new CuboidRegion(name);
        super.put(name, region);
        return region;
    }

    @Override
    public CuboidRegion createValueByKey(String key) {
        return new CuboidRegion(key, null);
    }

    public static File getRegionsDirectory() {
        return new File(GamesCoreAPI.getApiOptional()
                .map(api -> api.getPlugin().getDataFolder() + "/../../regions")
                .orElse("D:\\workspace\\SCore\\games-core\\test_data"));
    }

}
