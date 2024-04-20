package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.util.LocationUtils;

import java.util.*;

@Getter
public class Hologram {

    private final String name;
    private final HologramHolderConfig config;

    private final Location location;
    private final List<HologramArmorStand> linesStands = new ArrayList<>();

    protected Hologram(String name, HologramHolderConfig config) {
        this.name = name;
        this.config = config;
        this.location = loadLocation();
        loadAllConfigHolograms();
    }

    public List<HologramArmorStand> getLinesArmorStands() {
        return linesStands;
    }

    private Location loadLocation() {
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        return LocationUtils.stringToLocation(config.getString(locationPath));
    }

    private void loadAllConfigHolograms() {
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
        Collections.reverse(lines);
        for (String line : lines) {
            list.add(new HologramArmorStand(loc, line));
            loc.add(0, 0.3, 0);
        }
        return list;
    }

}
