package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.RegionsManager;

public class RegionsCommand extends GamesCoreCommand {

    public RegionsCommand() {
        super("regions");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        if (args.length == 0) {
            player.sendMessage("No valid args! create, list, tp, addmeta, removemeta");
            return;
        }

        RegionsManager regionsManager = RegionsManager.getManager(RegionsManager.class);
        if (regionsManager == null) {
            player.sendMessage("No region manager found!");
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
                regionsManager.createRegion(regionName, cuboid);
                player.sendMessage("Region created!");
            } else {
                player.sendMessage("Invalid number of arguments! Specify a region name.");
            }
        }

        if (args[0].equalsIgnoreCase("list")) {
            StringBuilder stringBuilder = new StringBuilder("List of regions:\n");
            regionsManager.values().forEach(region -> {
                stringBuilder.append(" - ").append(region.getName()).append("\n");
            });
            player.sendMessage(stringBuilder.toString());
        }

        if (args[0].equalsIgnoreCase("tp")) {
            regionsManager.getRegion(args[1]).ifPresentOrElse(region -> {
                player.teleport(region.getCuboid().getMin());
            }, () -> {
                player.sendMessage("No region found!");
            });
        }

        if (args[0].equalsIgnoreCase("addmeta")) {
            regionsManager.getRegion(args[1]).ifPresentOrElse(region -> {
                String metaValue = getLabelFromArgNumber(label, 2);
                region.getConfig().addMetaData(metaValue);
                region.getConfig().save();
                player.sendMessage("Added meta data!");
            }, () -> {
                player.sendMessage("No region found!");
            });
        }

        if (args[0].equalsIgnoreCase("removemeta")) {
            regionsManager.getRegion(args[1]).ifPresentOrElse(region -> {
                String metaValue = getLabelFromArgNumber(label, 2);
                region.getConfig().removeMetaData(metaValue);
                region.getConfig().save();
                player.sendMessage("Removed meta data!");
            }, () -> {
                player.sendMessage("No region found!");
            });

        }

    }

    private String getLabelFromArgNumber(String label, int startIndex) {
        StringBuilder builder = new StringBuilder();
        String[] args = label.split(" ");
        for (int i = startIndex; i < label.length(); i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString();
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
