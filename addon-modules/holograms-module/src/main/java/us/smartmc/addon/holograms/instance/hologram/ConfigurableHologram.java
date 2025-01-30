package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.util.LocationUtils;
import us.smartmc.addon.holograms.util.NPCModuleUtil;
import us.smartmc.npcsmodule.instance.CustomNPC;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableHologram extends Hologram implements IConfigurableHologram {

    @Getter
    private final String name;
    private final HologramHolderConfig config;

    // Create
    public ConfigurableHologram(HologramHolder holder, String name, Location location) {
        super(name, location);
        this.name = name;
        this.config = new HologramHolderConfig(holder);
        config.setLocation(name, location);
        setup();
    }

    // Load
    public ConfigurableHologram(String name, HologramHolderConfig config) {
        super(name, loadLocation(name, config));
        this.name = name;
        this.config = config;
        setup();
    }

    @Override
    public void setup() {
        location = loadLocation(name, config);
        loadAllConfigHolograms();
    }

    @Override
    public void assignToNPCLocation(String npcName) throws Exception {
        super.assignToNPCLocation(npcName);
        setup();
    }

    public static Location loadLocation(String name, HologramHolderConfig config) {
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        String locationString = config.getString(locationPath);

        Location location = null;
        if (locationString.startsWith("npc@")) {
            String npcName = locationString.split("@")[1];
            CustomNPC npc = NPCModuleUtil.getFirstByName(npcName);
            if (npc != null)
                location = npc.getBukkitLocation().clone().add(0, 0, 0);
            if (location == null) {
                return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
            }
        }
        return location == null ? LocationUtils.stringToLocation(locationString) : location;
    }

    @Override
    public void loadAllConfigHolograms() {
        String linesMainPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.LINES_KEY;
        List<String> lines = new ArrayList<>();
        for (String line : config.getConfig().getStringList(linesMainPath)) {
            if (line.contains("\n")) {
                lines.addAll(List.of(line.split("\n")));
                continue;
            }
            lines.add(line);
        }
        linesStands.addAll(getOf(lines));
    }

    private List<HologramArmorStand> getOf(List<String> lines) {
        List<HologramArmorStand> list = new ArrayList<>();
        Location loc = location.clone();
        for (String line : lines) {
            list.add(new HologramArmorStand(loc, line));
            loc.add(0, getLineSeparation(), 0);
        }
        return list;
    }

}
