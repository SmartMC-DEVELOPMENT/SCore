package us.smartmc.test.cmd;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.test.instance.RegionPacketSender;

import java.util.Arrays;

public class PasteRegionCommand extends GamesCoreCommand {

    public PasteRegionCommand() {
        super("pasteregion");
    }

    @Override
    public void performConsole(CommandSender sender, String label, String[] args) {

    }

    @Override
    public void performPlayer(Player sender, String label, String[] args) {

    }

    @Override
    public boolean perform(CommandSender sender, String s, String[] args) {
        System.out.println("ARGS=" + Arrays.toString(args));
        Location location = getLocationFromSender(sender, args);

        String name = args[0];
        long startRequest = System.currentTimeMillis();
        sender.sendMessage("Pasting...");
        GamesCoreAPI.getApi().getBackendConnection().getCuboid(name).thenAccept(cuboidGetResponse -> {

            long endRequest = System.currentTimeMillis();
            double duration = (endRequest - startRequest) / 1000.0;
            sender.sendMessage(ChatUtil.parse("&9OBTAINED CUBOID IN &b" + duration + "s"));


            try {
                CuboidWrapper wrapper = cuboidGetResponse.getWrapper();

                long startPaste = System.currentTimeMillis();

                CuboidPaster paster = new CuboidPaster(wrapper);

                paster.pasteAt(location, () -> {
                    long endPaste = System.currentTimeMillis();
                    double pasteDuration = (endPaste - startPaste) / 1000.0;
                    int count = wrapper.getBlocks().size();
                    sender.sendMessage(ChatUtil.parse("&cPasted AT &b" + pasteDuration + "s&c (" + count + " Blocks)"));

                    if (sender instanceof Player player) {
                        //RegionPacketSender.sendPasteChanges(player, location, paster);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return false;
    }

    public Location getLocationFromSender(CommandSender sender, String[] args) {
        if (sender instanceof Player player) return player.getLocation();
        World world = Bukkit.getWorlds().get(0);
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        int z = Integer.parseInt(args[3]);
        return new Location(world, x, y, z);
    }

}
