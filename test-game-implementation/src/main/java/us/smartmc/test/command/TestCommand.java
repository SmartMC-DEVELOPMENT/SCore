package us.smartmc.test.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.util.CuboidSerializer;
import us.smartmc.test.TestGameImplementation;

import java.io.File;

public class TestCommand implements CommandExecutor {

    private static Location loc1, loc2;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        try {
            if (args.length == 0) {
                sender.sendMessage("No valid args! l1, l2, saveregion, paste");
                return true;
            }

            if (sender instanceof Player player) {
                Location location = player.getLocation();

                File schemsDir = new File(TestGameImplementation.getPlugin(TestGameImplementation.class).getDataFolder() + "/schems");
                schemsDir.mkdirs();

                if (args[0].equalsIgnoreCase("l1")) {
                    loc1 = location;
                    player.sendMessage(ChatUtil.parse("&aPos1 establecido!"));
                }

                if (args[0].equalsIgnoreCase("l2")) {
                    loc2 = location;
                    player.sendMessage(ChatUtil.parse("&aPos2 establecido!"));
                }

                if (args[0].equalsIgnoreCase("saveregion")) {
                    File file = new File(schemsDir, args[1] + ".schem");
                    CuboidSerializer.serialize(new Cuboid(loc1, loc2), file.getAbsolutePath());
                    player.sendMessage(ChatUtil.parse("&aRegion guardada! " + file.getAbsolutePath()));
                }

                if (args[0].equalsIgnoreCase("paste")) {

                    File file = new File(schemsDir, args[1] + ".schem");
                    CuboidSerializer.deserializeAndPaste(file.getAbsolutePath(), location);
                    player.sendMessage(ChatUtil.parse("&aRegion pegada! " + file.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        return false;
    }
}
