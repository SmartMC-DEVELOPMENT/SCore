
package us.smartmc.addon.holograms.commands;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramArmorStand;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.List;

public class HologramsCommand extends AddonPluginCommand {

    public HologramsCommand() {
        super("holograms", true, "smartaddon.command.holograms");
    }

    @Override
    public void executeAll(CommandSender sender, String[] args) {
        String permission = getPermission();
        if (permission == null) return;
        if (!sender.hasPermission(permission)) return;

        if (args.length == 0) {
            sender.sendMessage("No valid subcommand!");
            return;
        }


        HologramHolder mainHolder = HologramHolder.getOrCreate("main");
        switch (args[0].toLowerCase()) {
            case "addline" -> {
                if (args.length <= 2) {
                    sender.sendMessage(PaperChatUtil.parse("&cYou have to specify text!"));
                    return;
                }
                String name = args[1];
                Hologram hologram = mainHolder.getHologram(name);
                String text = readText(2, args);
                for (String line : text.split("\n")) {
                    Location lastLoc = hologram.getLinesArmorStands().get(hologram.getLinesArmorStands().size() - 1).getStand().getLocation();
                    lastLoc.add(0, 0.3, 0);
                    hologram.getLinesArmorStands().add(new HologramArmorStand(lastLoc, line));
                }
                sender.sendMessage(PaperChatUtil.parse("&aAdded line!"));
                mainHolder.updateHologramConfig(name);
            }
            case "removeline" -> {
                if (args.length <= 2) {
                    sender.sendMessage(PaperChatUtil.parse("&cYou have to specify number!"));
                    return;
                }
                String name = args[1];
                int index = Integer.parseInt(args[2]);
                Hologram hologram = mainHolder.getHologram(name);
                HologramArmorStand hologramArmorStand = hologram.getLinesArmorStands().get(index);
                hologramArmorStand.getStand().remove();
                hologram.getLinesArmorStands().remove(index);

                sender.sendMessage(PaperChatUtil.parse("&aRemoved line!"));
                if (!hologram.getLinesArmorStands().isEmpty()) {
                    mainHolder.updateHologramConfig(name);
                } else {
                    mainHolder.deleteHologram(name);
                    sender.sendMessage(PaperChatUtil.parse("&aEmpty hologram has been detected and deleted! (" + name + ")"));
                }
            }

            case "setline" -> {
                if (args.length <= 2) {
                    sender.sendMessage(PaperChatUtil.parse("&cYou have to specify number & text!"));
                    return;
                }
                String name = args[1];
                int index = Integer.parseInt(args[2]);
                String text = readText(3, args);
                Hologram hologram = mainHolder.getHologram(name);
                HologramArmorStand hologramArmorStand = hologram.getLinesArmorStands().get(index);
                Location loc = hologramArmorStand.getStand().getLocation();

                hologramArmorStand.getStand().remove();
                hologram.getLinesArmorStands().set(index, new HologramArmorStand(loc, text));
                sender.sendMessage(PaperChatUtil.parse("&aSet line!"));
                mainHolder.updateHologramConfig(name);
            }

        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {

    }

    @Override
    public void executePlayer(Player player, String[] args) {

    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("No valid subcommand!");
            return;
        }

        HologramHolder mainHolder = HologramHolder.getOrCreate("main");

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length <= 2) {
                    player.sendMessage("You need to specify text for the hologram!");
                    return;
                }
                String name = args[1];
                String text = readText(2, args);
                mainHolder.registerHologram(name, player.getLocation(), text);
                player.sendMessage(PaperChatUtil.parse("&aHologram added!"));
            }
            case "delete" -> {
                String name = args[1];
                mainHolder.deleteHologram(name);
                player.sendMessage(PaperChatUtil.parse("&aHologram deleted!"));
            }

            case "teleport" -> {
                String name = args[1];
                Hologram hologram = mainHolder.getHologram(name);
                player.teleport(hologram.getLinesArmorStands().get(0).getStand().getLocation());
                player.sendMessage(PaperChatUtil.parse("&aTeleported!"));
            }
        }

    }

    private String readText(int startIndex, String[] args) {
        StringBuilder b = new StringBuilder();
        for (int index = startIndex; index < args.length; index++) {
            b.append(args[index]);
            b.append(" ");
        }
        b.deleteCharAt(b.length() - 1);
        return b.toString();
    }

    @NotNull
    @Override
    public List<String> getAliases() {
        return List.of("hd", "h", "hologram");
    }
}
