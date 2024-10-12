package us.smartmc.gamescore.cmd;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.gamescore.adminplayer.PlayerSelectSession;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboidRegion;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.game.WaitingLobbySession;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.manager.player.PlayerSelectionsManager;
import us.smartmc.gamescore.menu.ManageMetadataRegionMenu;
import us.smartmc.gamescore.util.EditorModeUtil;

public class SaveWaitingLobbyCommand extends GamesCoreCommand {

    public SaveWaitingLobbyCommand() {
        super("savewaitinglobby");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        PlayerSelectionsManager selectionsManager = MapManager.getManager(PlayerSelectionsManager.class);
        if (selectionsManager == null) return;
        PlayerSelectSession session = selectionsManager.get(player.getUniqueId());
        if (!EditorModeUtil.hasWandSelection(player)) {
            player.sendMessage(ChatUtil.color("&cYou need to have a complete region selected to save waiting lobby region"));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatUtil.color("&cYou need to specify a name for this waiting lobby region"));
            return;
        }

        String name = args[0];

        BukkitCuboid cuboid = session.buildCuboid();
        WaitingLobbySession.saveWaitingLobbyRegion(name, cuboid, res -> {
            if (res.getResponse() == CuboidSaveResponse.RequestResponse.ERROR) {
                player.sendMessage(ChatUtil.color("&cFailed to save waiting lobby region!"));
                return;
            }
            player.sendMessage(ChatUtil.color("&aSaved waiting lobby region!"));
        });
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
