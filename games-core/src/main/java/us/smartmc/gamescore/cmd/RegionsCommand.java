package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.RegionsManager;

public class RegionsCommand extends GamesCoreCommand {

    public RegionsCommand() {
        super("regions");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        if (args.length == 0) {
            player.sendMessage("No valid args! create, list");
            return;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length >= 2) {
                String regionName = args[1];
                PlayerRegionSelectSession selectSession = PlayerRegionSelectSession.get(player);
                if (selectSession == null) {
                    player.sendMessage("No session found!");
                    return;
                }

                if (selectSession.getPos1() == null || selectSession.getPos2() == null) {
                    player.sendMessage("Pos 1 or 2 are null! Mark!");
                }
                Cuboid cuboid = selectSession.buildCuboid();
                RegionsManager regionsManager = MapManager.getManager(RegionsManager.class);
                if (regionsManager == null) {
                    player.sendMessage("No region manager found!");
                    return;
                }
                regionsManager.createRegion(regionName, cuboid);
                player.sendMessage("Region created!");
            } else {
                player.sendMessage("Invalid number of arguments! Specify a region name.");
            }
        }

        if (args[0].equalsIgnoreCase("list")) {
            RegionsManager regionsManager = RegionsManager.getManager(RegionsManager.class);
            if (regionsManager == null) {
                player.sendMessage("No region manager found!");
                return;
            }

            StringBuilder stringBuilder = new StringBuilder("List of regions:\n");
            regionsManager.values().forEach(region -> {
                stringBuilder.append(" - ").append(region.getName()).append("\n");
            });
            player.sendMessage(stringBuilder.toString());
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
