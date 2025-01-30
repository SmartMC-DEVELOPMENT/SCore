package us.smartmc.addon.holograms.instance.hologram;

import org.bukkit.Location;

import java.util.List;

public interface IHologram {

    String getName();

    double getLineSeparation();

    void updateLine(int index, String line);

    void removeAllStands();

    List<HologramArmorStand> getLinesArmorStands();

    void assignToNPCLocation(String npcName) throws Exception;

    void addLine(String text);

    Location getLocation();

}
