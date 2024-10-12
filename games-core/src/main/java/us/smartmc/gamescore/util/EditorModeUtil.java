package us.smartmc.gamescore.util;

import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;

public class EditorModeUtil {

    public static void leaveEditorMode(Player player) {
        {
            // Menu set
            GUIMenu menu = GUIMenu.getSetGUI(player);
            if (menu == null) return;
            if (menu instanceof EditMapInventoryMenu inv) {
                inv.leave(player);
                GUIMenu.unregisterSetGUI(player.getUniqueId());
            }
        }
        {
            // Edit session
            EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
            if (manager == null) return;
            manager.remove(player.getUniqueId());
        }
    }

}
