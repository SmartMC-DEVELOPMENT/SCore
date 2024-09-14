package us.smartmc.addon.holograms.instance.hologram;

import org.bukkit.Location;
import us.smartmc.core.exception.CorePluginException;

import java.util.List;

public interface IHologram {

    String getName();

    double getLineSeparation();

    void removeAllStands();

    List<HologramArmorStand> getLinesArmorStands();

    void assignToNPCLocation(String npcName) throws CorePluginException;

    void addLine(String text);

    Location getLocation();

}
