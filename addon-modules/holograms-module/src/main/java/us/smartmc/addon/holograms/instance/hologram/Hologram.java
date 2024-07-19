package us.smartmc.addon.holograms.instance.hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.util.LocationUtils;
import us.smartmc.addon.holograms.util.NPCModuleUtil;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.npcsmodule.instance.CustomNPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hologram {

    private final String name;
    private final HologramHolderConfig config;

    private Location location;
    private final List<HologramArmorStand> linesStands = new ArrayList<>();

    protected Hologram(String name, HologramHolderConfig config) {
        this.name = name;
        this.config = config;
        setup();
    }

    private void setup() {
        this.location = loadLocation();
        loadAllConfigHolograms();
    }

    public void removeAllStands() {
        for (HologramArmorStand stand : new ArrayList<>(linesStands)) {
            stand.getStand().remove();
            linesStands.remove(stand);
        }
    }

    public void assignToNPCLocation(String npcName) throws CorePluginException {
        CustomNPC npc = NPCModuleUtil.getFirstByName(npcName);
        if (npc == null) throw new CorePluginException("No NPC found with name of '" + npcName + "'!");
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        config.set(locationPath, "npc@" + npcName);
        config.save();
        removeAllStands();
        setup();
    }

    public void addLine(Location location, String text) {
        linesStands.add(new HologramArmorStand(location, text));
    }

    public List<HologramArmorStand> getLinesArmorStands() {
        return linesStands;
    }

    private Location loadLocation() {
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        String locationString = config.getString(locationPath);

        Location npcLocation = null;
        if (locationString.startsWith("npc@")) {
            String name = locationString.split("@")[1];
            CustomNPC npc = NPCModuleUtil.getFirstByName(name);
            if (npc != null)
                npcLocation = npc.getBukkitLocation().clone().add(0, 0.8, 0);
            if (npcLocation == null) {
                return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
            }
        }
        return npcLocation == null ? LocationUtils.stringToLocation(locationString) : npcLocation;
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
        List<String> reversedLines = new ArrayList<>(lines);
        Collections.reverse(reversedLines);

        for (String line : reversedLines) {
            list.add(new HologramArmorStand(loc, line));
            loc.add(0, 0.3, 0);
        }
        return list;
    }

}
