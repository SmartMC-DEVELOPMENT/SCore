package us.smartmc.gamescore.util;

import org.bukkit.Location;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

public class CuboidPaster {

    public static void paste(CuboidWrapper wrapper, Location location) {
        wrapper.pasteAtLocation(location);
    }
}
