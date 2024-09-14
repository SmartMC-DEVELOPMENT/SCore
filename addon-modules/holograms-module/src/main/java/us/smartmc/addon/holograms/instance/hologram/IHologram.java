package us.smartmc.addon.holograms.instance.hologram;

import org.bukkit.Location;
import us.smartmc.core.exception.CorePluginException;

import java.util.List;

public interface IHologram {

    void setup();

    String getName();

    void removeAllStands();

    List<HologramArmorStand> getLinesArmorStands();

    void assignToNPCLocation(String npcName) throws CorePluginException;

    void addLine(Location location, String text);

    Location getLocation();

}
