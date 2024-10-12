package us.smartmc.gamescore.util;

import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Vector3i;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.PlayerRegionSelectionsManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;

public class EditorModeUtil {

    public static Vector3i getRelativePosition(Player player, Location location) {
        BukkitCuboid cuboid = getBukkitCuboidFromSelection(player);
        if (cuboid == null) return null;
        return cuboid.getRelativeCoordinates(location);
    }

    public static BukkitCuboid getBukkitCuboidFromSelection(Player player) {
        PlayerRegionSelectSession session = getSelectionSession(player);
        if (session == null) return null;
        if (session.getPos1() == null || session.getPos2() == null) return null;
        return new BukkitCuboid(session.getPos1(), session.getPos2());
    }

    public static PlayerRegionSelectSession getSelectionSession(Player player) {
        PlayerRegionSelectionsManager manager = MapManager.getManager(PlayerRegionSelectionsManager.class);
        if (manager == null) return null;
        return manager.get(player.getUniqueId());

    }

    public static boolean hasWandSelection(Player player) {
        PlayerRegionSelectSession session = getSelectionSession(player);
        if (session == null) return false;
        return session.getPos2() != null && session.getPos1() != null;
    }

    public static void leaveEditorMode(Player player) {
        // Menu
        GUIMenu.unregisterSetGUI(player.getUniqueId());
        {
            // Edit session
            EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
            if (manager == null) return;
            manager.remove(player.getUniqueId());
        }
    }

}
