package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Location;
import us.smartmc.addon.holograms.util.NPCModuleUtil;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.npcsmodule.instance.CustomNPC;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hologram implements IHologram {

    protected final String name;

    protected Location location;

    protected final List<HologramArmorStand> linesStands = new ArrayList<>();

    public Hologram(String name, Location location) {
        this.name = name;
        this.location = location;
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
        return List.of();
    }

    @Override
    public void assignToNPCLocation(String npcName) throws CorePluginException {
        CustomNPC npc = NPCModuleUtil.getFirstByName(npcName);
        if (npc == null) throw new CorePluginException("No NPC found with name of '" + npcName + "'!");
        location = npc.getBukkitLocation().clone().add(0, -0.2, 0);
        removeAllStands();
    }

    @Override
    public void addLine(Location location, String text) {
        linesStands.add(new HologramArmorStand(location, text));
    }
}
