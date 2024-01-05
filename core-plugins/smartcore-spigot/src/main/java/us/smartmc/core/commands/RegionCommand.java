package us.smartmc.core.commands;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.regions.Cuboid;
import us.smartmc.core.regions.CuboidManager;
import us.smartmc.core.regions.RegionConfig;
import us.smartmc.core.regions.controller.CuboidEditorPlayerSession;
import us.smartmc.core.regions.controller.RegionModeManager;

import static us.smartmc.core.util.ItemUtils.wand;

public class RegionCommand implements CommandExecutor {

    private static final SmartCore plugin = SmartCore.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!sender.hasPermission("smartcore.admin")) {
                player.sendMessage(ChatUtil.parse(player, "<lang.general.noPermission>"));
                return true;
            }

            if (args.length == 0) {
                player.sendMessage("Subargs: wand, create <name>, delete <name>");
                return true;
            }

            if (args[0].equals("wand")) {
                player.getInventory().addItem(wand);
                return true;
            }


            CuboidManager cuboidManager = plugin.getCuboidManager();
            String regionName = args[1];

            if (args[0].equals("create")) {
                RegionModeManager regionModeManager = plugin.getRegionModeManager();

                if (!regionModeManager.isRegistered(player.getUniqueId())) {
                    regionModeManager.register(player.getUniqueId(), new CuboidEditorPlayerSession(player));
                }
                CuboidEditorPlayerSession session = regionModeManager.get(player.getUniqueId());
                Cuboid editorCuboid = session.getEditorCuboid();
                if (editorCuboid.getLoc1() != null && editorCuboid.getLoc2() != null) {
                    RegionConfig regionConfig = new RegionConfig(regionName);
                    regionConfig.setCuboidRegion(editorCuboid);
                    regionConfig.save();
                    cuboidManager.register(regionName, regionConfig);
                    regionConfig.save();
                    player.sendMessage(ChatUtil.parse(player, "&aRegion created! &e{0}", regionName));
                } else {
                    player.sendMessage(ChatUtil.parse(player, "&cA position is missing! &eLEFT &cfor position 1 and &eRIGHT &cfor position 2"));
                }
            }

            if (args[0].equals("delete")) {
                RegionConfig regionConfig = cuboidManager.get(regionName);
                if (regionConfig == null) {
                    regionConfig.getFile().delete();
                    regionConfig.save();
                    cuboidManager.unregister(regionName);
                    player.sendMessage(ChatUtil.parse(player, "&aRegion deleted! &e{0}", regionName));
                    return true;
                }
                Bukkit.getOnlinePlayers().forEach(p -> {
                    SmartCorePlayer corePlayer = SmartCorePlayer.get(p);
                    corePlayer.getPlayerRegionSubscriber().getRegionsAt().remove(regionConfig);
                });
            }

            return true;
        }

        sender.sendMessage(ChatUtil.parse("&cThis command is only allowed for players!"));
        return false;
    }
}