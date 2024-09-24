package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.util.CuboidSerializer;

import java.io.File;

public class SchemsCommand extends GamesCoreCommand {

    public SchemsCommand() {
        super("schems");
    }

    private static File getSchemsDir() {
        return new File(GamesCoreAPI.getApi().getPlugin().getDataFolder() + "/schems");
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
            getSchemsDir().mkdirs();
            CuboidSerializer.serialize(cuboid, new File(getSchemsDir(), name + ".dat").getAbsolutePath());
        }

        if (args[0].equalsIgnoreCase("paste")) {
            String name = args[1];
            getSchemsDir().mkdirs();
            CuboidSerializer.deserializeAndPaste(new File(getSchemsDir(), name + ".dat").getAbsolutePath(), player.getLocation());
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
