package us.smartmc.npcsmodule.command;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.smartmc.core.SmartCore;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.npcsmodule.util.NPCUtil;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class NPCCommand extends AddonPluginCommand {

    public NPCCommand() {
        super("npc", true, "npc.command");
    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {
        performBoth(player, args);

        if (args.length == 0) {
            player.sendMessage("No valid args.");
            return;
        }

        Location location = player.getLocation();
        NPCManager mainManager = NPCManager.getManagers().get(0);

        switch (args[0].toLowerCase()) {
            case "create", "add", "crear", "añadir" -> {
                String idName = NPCUtil.getNameOrDefault(args[1]);
                try {
                    mainManager.register(idName, NPCUtil.getDefaultCustomNPC(location, idName));
                    player.sendMessage("Created!");
                } catch (CorePluginException e) {
                    player.sendMessage("Error ocurred: " + e.getMessage());
                }
            }
            case "tp" -> {
                CustomNPC npc = getCustomNPCById(args[1]);
                if (npc == null) {
                    player.sendMessage("NPC not found!");
                    return;
                }
                Location npcLocation = npc.getBukkitLocation();
                player.teleport(npcLocation);
                player.sendMessage("Teleported!");
            }
            case "tphere" -> {
                CustomNPC npc = getCustomNPCById(args[1]);
                if (npc == null) {
                    player.sendMessage("NPC not found!");
                    return;
                }
                npc.updateLocation(location);
                player.sendMessage("Teleported and saved!");
                updateNPCVisibility(npc, true);
            }
        }
    }

    @Override
    public void executeConsole(CommandSender commandSender, String[] args) {
        performBoth(commandSender, args);

    }

    public void performBoth(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No valid args.");
            return;
        }

        NPCManager mainManager = NPCManager.getManagers().get(0);

        switch (args[0].toLowerCase()) {
            case "delete", "remove", "eliminar", "remover", "quitar" -> {
                String name = NPCUtil.getNameOrDefault(args[1]);
                CustomNPC npc = mainManager.delete(name);
                if (npc == null) {
                    sender.sendMessage("Not npc found to delete!");
                    return;
                }
                sender.sendMessage("Deleted!");
                updateNPCVisibility(npc, false);
            }
        }
    }

    @Override
    public void executeAll(CommandSender commandSender, String[] args) {
    }

    @Override
    public void executePlayer(Player player, String[] args) {
    }

    public static void updateNPCVisibility(CustomNPC npc, boolean showAgain) {
        ServerPlayer npcPlayer = npc.getNpcPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hideEntity(SmartCore.getPlugin(), npcPlayer.getBukkitEntity());
            if (showAgain)
                player.showEntity(SmartCore.getPlugin(), npcPlayer.getBukkitEntity());
        }
    }

    public static CustomNPC getCustomNPCById(String id) {
        if (id == null) return null;
        CustomNPC npc = null;
        for (NPCManager npcManager : NPCManager.getManagers()) {
            for (CustomNPC managerNpc : npcManager.values()) {
                // Find by configId
                if (managerNpc.getConfigId().equals(id)) {
                    npc = managerNpc;
                    break;
                }
                // Find by entity name
                if (managerNpc.getNpcPlayer().getGameProfile().getName().equals(id)) {
                    npc = managerNpc;
                    break;
                }
            }
        }
        return npc;
    }

}
