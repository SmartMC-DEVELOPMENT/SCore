package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.io.File;

public class SchemsCommand extends GamesCoreCommand {

    public SchemsCommand() {
        super("schems");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Invalid args! save, paste");
            return;
        }

        if (args[0].equalsIgnoreCase("save")) {
            String name = args[1];
            PlayerRegionSelectSession selectSession = PlayerRegionSelectSession.get(player);
            if (selectSession == null) {
                player.sendMessage("No session found!");
                return;
            }

            if (selectSession.getPos1() == null || selectSession.getPos2() == null) {
                player.sendMessage("Pos 1 or 2 are null! Mark!");
            }
            Cuboid cuboid = selectSession.buildCuboid();
            //CuboidPaster.serialize(cuboid, new File(getSchemsDir(), name + ".dat").getAbsolutePath());
        }

        if (args[0].equalsIgnoreCase("paste")) {
            String name = args[1];
            //CuboidPaster.deserializeAndPaste(new File(getSchemsDir(), name + ".dat").getAbsolutePath(), player.getLocation());
        }
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
