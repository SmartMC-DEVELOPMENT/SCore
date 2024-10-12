package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.game.map.EditMapSession;
import us.smartmc.gamescore.menu.EditMapSelectionMenu;
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
