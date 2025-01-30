package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import us.smartmc.addon.holograms.util.NPCModuleUtil;
import us.smartmc.npcsmodule.instance.CustomNPC;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hologram implements IHologram {

    protected final String name;

    @Setter
    protected Location location;

    private double lineSeparation = -0.3;

    protected final List<HologramArmorStand> linesStands = new ArrayList<>();

    public Hologram(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public void setLineSeparation(double lineSeparation) {
        this.lineSeparation = lineSeparation;

        // Recover unformatted lines, then remove method & add lines again with new separator
        List<String> lines = new ArrayList<>();
        for (HologramArmorStand armorStand : linesStands) {
            lines.add(armorStand.getUnformattedLine());
        }
        removeAllStands();
        lines.forEach(this::addLine);
    }

    @Override
    public void updateLine(int index, String line) {
        getLinesArmorStands().get(index).setUnformattedLine(line);
    }

    @Override
    public void removeAllStands() {
        for (HologramArmorStand stand : new ArrayList<>(linesStands)) {
            stand.getStand().eject();
            stand.getStand().remove();
            linesStands.remove(stand);
        }
    }

    @Override
    public List<HologramArmorStand> getLinesArmorStands() {
        return linesStands;
    }

    @Override
    public void assignToNPCLocation(String npcName) throws Exception {
        CustomNPC npc = NPCModuleUtil.getFirstByName(npcName);
        if (npc == null) throw new Exception("No NPC found with name of '" + npcName + "'!");
        location = npc.getBukkitLocation().clone().add(0, -0.2, 0);
        removeAllStands();
    }

    @Override
    public void addLine(String text) {
        Location lastLoc;
        if (linesStands.isEmpty()) {
            lastLoc = location.clone();
        } else {
            lastLoc = linesStands.get(linesStands.size() - 1).getStand().getLocation();
            lastLoc.add(0, lineSeparation, 0);
        }
        linesStands.add(new HologramArmorStand(lastLoc, text));
    }
}
