package us.smartmc.game.luckytowers.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.util.BukkitChatUtil;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.player.EditorSession;
import us.smartmc.game.luckytowers.manager.EditorModeManager;
import us.smartmc.game.luckytowers.messages.AdminMessages;

public class AdminEditorCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.player();
        EditorModeManager manager = LuckyTowers.getManager(EditorModeManager.class);
        EditorSession editSession = manager.get(player.getUniqueId());

        GameMap map = editSession.getMap();

        if (map == null) {
            BukkitChatUtil.send(player, AdminMessages.editor_unknownMap);
            return;
        }

        IMessageCategory feedbackMessage = null;
        Object[] feedbackArgs = {};
        switch (args[0]) {
            case "setSpawn" -> {
                map.setSpawn(player.getLocation());
                feedbackMessage = AdminMessages.editor_spawnSet;
            }

            case "addTeamSpawn" -> {
                Location initLocation = player.getLocation();
                map.getSpawnLocations().add(initLocation);
                map.saveSpawnLocations();
                player.sendBlockChange(initLocation, Material.BEACON, (byte) 0);
                player.teleport(initLocation);
                feedbackMessage = AdminMessages.editor_spawnAdded;
            }

            case "removeLastSpawn" -> {
                map.getSpawnLocations().remove(map.getSpawnLocations().size() - 1);
                map.saveSpawnLocations();
                feedbackMessage = AdminMessages.editor_spawnRemovedLast;
            }

            case "setCorner" -> {
                if (handler.interactAction().name().contains("RIGHT")) {
                    map.setPos1(player.getLocation());
                    feedbackArgs = new Object[]{"1"};
                } else {
                    map.setPos2(player.getLocation());
                    feedbackArgs = new Object[]{"2"};
                }
                feedbackMessage = AdminMessages.editor_cornerSet;
            }

            case "toggleMaintenance" -> {
                boolean activated = map.toggleMaintenance();
                feedbackMessage = AdminMessages.editor_maintenanceSet;
                String prefix = activated ? "&a" : "&c";
                feedbackArgs = new Object[]{prefix + activated};
            }

            case "saveMap" -> {
                try {
                    player.teleport(map.getSpawn(player.getWorld(), 0));
                    editSession.saveRegion(player.getWorld());
                    feedbackMessage = AdminMessages.editor_regionSaved;
                } catch (Exception e) {
                    e.printStackTrace();
                    feedbackMessage = AdminMessages.editor_regionSaveError;
                    feedbackArgs = new String[]{e.getMessage()};
                }
            }

        }
        if (feedbackMessage != null) BukkitChatUtil.send(player, feedbackMessage, feedbackArgs);
    }
}
