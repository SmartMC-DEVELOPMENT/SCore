package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import me.imsergioh.pluginsapi.language.Language;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.util.LocationUtils;

import java.util.*;

@Getter
public class Hologram {

    private final String name;
    private final HologramHolderConfig config;

    private final Location location;
    private final Map<String, List<String>> unformattedLines = new HashMap<>();

    protected Hologram(String name, HologramHolderConfig config) {
        this.name = name;
        this.config = config;
        this.location = loadLocation();
        loadUnformattedLines();
    }

    public List<String> getFormattedLines(String linesKey) {
        if (!unformattedLines.containsKey(linesKey)) {
            Optional<String> optional = unformattedLines.keySet().stream().findFirst();
            if (optional.isEmpty()) return List.of("");
            linesKey = optional.get();
        }
        return unformattedLines.get(linesKey);
    }

    public List<String> getFormattedLines(Language language) {
        return getFormattedLines(language.name());
    }

    private Location loadLocation() {
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.LINES_KEY;
        return LocationUtils.stringToLocation(config.getString(locationPath));
    }

    private void loadUnformattedLines() {
        String linesMainPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        for (String langId : Objects.requireNonNull(config.getConfig().getConfigurationSection(linesMainPath)).getKeys(false)) {
            String langLinesPath = linesMainPath + "." + langId;
            List<String> lines = config.getStringList(langLinesPath);
            unformattedLines.put(langId, lines);
        }
    }

}
