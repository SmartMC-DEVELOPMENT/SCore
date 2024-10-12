package us.smartmc.gamescore.cmd;

import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboidRegion;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.game.map.EditMapSession;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;
import us.smartmc.gamescore.menu.EditMapSelectionMenu;
import us.smartmc.gamescore.menu.ManageMetadataRegionMenu;
import us.smartmc.gamescore.util.EditorModeUtil;

public class EditMapCommand extends GamesCoreCommand {

    public EditMapCommand() {
        super("editmap");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {

        // Leave
        EditMapSession session = EditorModeUtil.getEditMapSession(player);
        if (session != null) {
            EditorModeUtil.leaveEditorMode(player);
            return;
        }

        // Join
        new EditMapSelectionMenu(player).open(player);
    }

    @Override
    public boolean perform(CommandSender sender, String label, String[] args) {
        return false;
    }

    @Override
    public String getPermission() {
        return "*";
    }
}
