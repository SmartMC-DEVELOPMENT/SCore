package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.backend.gamescore.CuboidSaveRequest;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboidRegion;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.manager.RegionsManager;

public class RegionsCommand extends GamesCoreCommand {

    public RegionsCommand() {
        super("regions");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        if (args.length == 0) {
            player.sendMessage("No valid args! create, list, tp, addmeta, removemeta, test");
            return;
        }

        RegionsManager regionsManager = RegionsManager.getManager(RegionsManager.class);
        if (regionsManager == null) {
            player.sendMessage("No region manager found!");
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
                return;
            }

            BukkitCuboid cuboid = new BukkitCuboid(selectSession.getPos1(), selectSession.getPos2());
            GamesCoreAPI.getApi().getBackendConnection().sendCuboid(name, cuboid).thenAccept(res -> {
                player.sendMessage("GETTED RES! " + res.getResponse().name());
            });

            GamesCoreAPI.getApi().getBackendConnection().sendObject("test command");
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
                BukkitCuboid cuboid = selectSession.buildCuboid();
                regionsManager.createBukkitRegion(regionName, cuboid);
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

            CuboidRegion region = regionsManager.get(args[1]);
            if (region != null) {
                if (region instanceof BukkitCuboidRegion bRegion) {
                    player.teleport(bRegion.getCuboid().getMinLocation());
                }
            } else player.sendMessage("No region found!");
        }

        if (args[0].equalsIgnoreCase("addmeta")) {
            regionsManager.getRegion(args[1]).ifPresentOrElse(region -> {
                String metaValue = getLabelFromArgNumber(label, 2);
                region.getDefaultConfig().addMetaData(metaValue);
                region.getDefaultConfig().save();
                player.sendMessage("Added meta data!");
            }, () -> {
                player.sendMessage("No region found!");
            });
        }

        if (args[0].equalsIgnoreCase("removemeta")) {
            regionsManager.getRegion(args[1]).ifPresentOrElse(region -> {
                String metaValue = getLabelFromArgNumber(label, 2);
                region.getDefaultConfig().removeMetaData(metaValue);
                region.getDefaultConfig().save();
                player.sendMessage("Removed meta data!");
            }, () -> {
                player.sendMessage("No region found!");
            });

        }

    }

    private String getLabelFromArgNumber(String label, int startIndex) {
        StringBuilder builder = new StringBuilder();
        String[] args = label.split(" ");

        // Verifica si el índice de inicio es válido
        if (startIndex < 0 || startIndex >= args.length) {
            return ""; // o manejar el error de otra manera
        }

        // Cambia la condición del bucle para evitar el ArrayIndexOutOfBoundsException
        for (int i = startIndex; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        return builder.toString().trim(); // Elimina el espacio al final
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
