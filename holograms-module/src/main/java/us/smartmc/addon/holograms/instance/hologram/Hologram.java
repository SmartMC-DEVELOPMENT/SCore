package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.util.LocationUtils;

import java.util.*;

@Getter
public class Hologram {

    private final String name;
    private final HologramHolderConfig config;

    private final Location location;
    private final Map<String, List<HologramArmorStand>> linesStands = new HashMap<>();

    protected Hologram(String name, HologramHolderConfig config) {
        this.name = name;
        this.config = config;
        this.location = loadLocation();
        loadAllConfigHolograms();
    }

    public List<HologramArmorStand> getLinesArmorStands(String linesKey) {
        return linesStands.get(linesKey);
    }

    public List<HologramArmorStand> getLinesArmorStands(Language language) {
        return getLinesArmorStands(language.name());
    }

    private Location loadLocation() {
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        System.out.println("LOADLOCATION path = " + locationPath);
        return LocationUtils.stringToLocation(config.getString(locationPath));
    }

    private void loadAllConfigHolograms() {
        String linesMainPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        for (String langId : Objects.requireNonNull(config.getConfig().getConfigurationSection(linesMainPath)).getKeys(false)) {
            String langLinesPath = linesMainPath + "." + langId;
            List<String> lines = config.getStringList(langLinesPath);
            linesStands.put(langId, getOf(lines));
        }
    }

    private List<HologramArmorStand> getOf(List<String> lines) {
        List<HologramArmorStand> list = new ArrayList<>();
        for (String line : lines) {
            list.add(new HologramArmorStand(location, line));
            location.add(0, 0.3, 0);
        }
        return list;
    }

}
