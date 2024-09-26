package us.smartmc.test.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.gamescore.util.RegionUtils;

public class PasteRegionCommand extends GamesCoreCommand {

    public PasteRegionCommand() {
        super("pasteregion");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        String name = args[0];
        GamesCoreAPI.getApi().getBackendConnection().getCuboid(name).thenAccept(cuboidGetResponse -> {
            player.sendMessage("Pasting...");
            try {
                CuboidWrapper wrapper = cuboidGetResponse.getWrapper();
                RegionUtils.pasteAtLocation(player.getLocation(), wrapper);
                player.sendMessage("Pasted!?");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean perform(CommandSender sender, String s, String[] args) {
        return false;
    }
}
