package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.joml.Vector3i;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapData;
import us.smartmc.gamescore.instance.game.map.spawn.ListSpawnsHolder;
import us.smartmc.gamescore.instance.game.map.spawn.OneSpawnHolder;
import us.smartmc.gamescore.instance.game.map.spawn.RegionSpawnHolder;
import us.smartmc.gamescore.instance.game.map.spawn.SpawnType;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;
import us.smartmc.gamescore.menu.EditorColorTeamsMenu;
import us.smartmc.gamescore.util.EditorModeUtil;

import java.util.List;

public class EditMapInvCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {

        try {
            handler.clickEvent().setCancelled(true);
        } catch (Exception ignore) {
        }

        try {
            handler.interactEvent().setCancelled(true);
        } catch (Exception ignore) {
        }

        // Add & subtract handle:
        if (args.length == 1) {
            String key = switch (args[0]) {
                case "max" -> GameMapData.MAX_PLAYERS;
                case "min" -> GameMapData.MIN_PLAYERS;
                case "teamLimit" -> GameMapData.TEAMS_LIMIT;
                default -> null;
            };
            if (key != null) {
                EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
                GameMap map = menu.getMap();
                addSubtract(handler.player(), handler.interactEvent(), key, map);
                return;
            }
        }

        if (args.length == 1 && args[0].equals("paste")) {
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameMap map = menu.getMap();
            EditMapSessionsManager manager = EditMapSessionsManager.getManager(EditMapSessionsManager.class);
            if (manager == null) return;
            manager.loadMapAt(handler.player(), map.getName());
            return;
        }

        if (args.length == 1 && args[0].equals("save")) {
            EditMapSessionsManager manager = EditMapSessionsManager.getManager(EditMapSessionsManager.class);
            if (manager == null) return;
            manager.saveMap(handler.player());
            return;
        }

        if (args.length == 1 && args[0].equals("teamNames")) {
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameMap map = menu.getMap();
            new EditorColorTeamsMenu(handler.player(), map).open(handler.player());
            return;
        }

        if (args.length == 1 && args[0].equals("maintenance")) {
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameMap map = menu.getMap();
            boolean enabled = map.isEnabled();
            map.getData().set(GameMapData.ENABLED, !enabled);
            map.getData().save();
            String message = !enabled ? "&aEnabled" : "&cDisabled";
            handler.clicker().sendMessage(ChatUtil.color(message));
            menu.load();
            menu.set(handler.player());
            return;
        }

        if (args.length == 1 && args[0].equals("toggleSpawnType")) {
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameMap map = menu.getMap();
            SpawnType newType = map.getData().getSpawnsData().toggleSpawnType();
            map.getData().save();
            String message = "&aChanged to: &f" + newType.name();
            handler.clicker().sendMessage(ChatUtil.color(message));
            menu.load();
            menu.set(handler.player());
            return;
        }

        if (args.length == 1 && args[0].equals("toggleTeam")) {
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameTeam newTeam = menu.toggleTeam();
            String name = newTeam == null ? "null" : newTeam.getName();
            String message = "&aChanged to: &f" + name;
            handler.clicker().sendMessage(ChatUtil.color(message));
            menu.load();
            menu.set(handler.player());
            return;
        }

        if (args.length == 1 && args[0].equals("removeLastPos")) {
            Player player = handler.player();
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameMap map = menu.getMap();
            if (map.getData().getSpawnsData().getHolder() instanceof ListSpawnsHolder holder) {
                List<Vector3i> posList = holder.getPositions(menu.getTeam());
                posList.remove(posList.size() - 1);
                holder.savePositions(menu.getTeam(), posList);
            }
        }

        if (args.length == 1 && args[0].equals("addSetPosition")) {
            Player player = handler.player();
            EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(handler.player());
            GameMap map = menu.getMap();
            PlayerRegionSelectSession selectSession = EditorModeUtil.getSelectionSession(player);
            if (selectSession == null) {
                player.sendMessage(ChatUtil.color("&cYou don't have a current selection! (Select the map region, save and add/set relative position)"));
                return;
            }

            // One
            if (map.getData().getSpawnsData().getHolder() instanceof OneSpawnHolder holder) {
                try {
                    Vector3i relativePos = EditorModeUtil.getRelativePosition(player, player.getLocation());
                    holder.setPosition(menu.getTeam(), relativePos);
                    player.sendMessage(ChatUtil.color("&aPosition set/added!"));
                } catch (Exception ignore) {
                    player.sendMessage(ChatUtil.color("&cSomething went wrong! (Make sure you are inside the map selected region)"));
                }

            }

            // Region
            if (map.getData().getSpawnsData().getHolder() instanceof RegionSpawnHolder holder) {
                boolean min = handler.clickEvent().isLeftClick();
                try {
                    Vector3i relativePos = EditorModeUtil.getRelativePosition(player, player.getLocation());
                    if (min) {
                        holder.setMinRelative(menu.getTeam(), relativePos);
                        player.sendMessage(ChatUtil.color("&aMin set!"));
                    } else {
                        holder.setMaxRelative(menu.getTeam(), relativePos);
                        player.sendMessage(ChatUtil.color("&aMax set!"));
                    }
                } catch (Exception ignore) {
                    player.sendMessage(ChatUtil.color("&cSomething went wrong! (Make sure you are inside the map selected region)"));
                }

            }

            // List
            if (map.getData().getSpawnsData().getHolder() instanceof ListSpawnsHolder holder) {
                try {
                    Vector3i relativePos = EditorModeUtil.getRelativePosition(player, player.getLocation());
                    holder.addPosition(menu.getTeam(), relativePos);
                } catch (Exception ignore) {
                    player.sendMessage(ChatUtil.color("&cSomething went wrong! (Make sure you are inside the map selected region)"));
                }

            }

            map.getData().save();
            SpawnType newType = map.getData().getSpawnsData().getSpawnType();
            String message = "&aChanged to: &f" + newType.name();
            handler.clicker().sendMessage(ChatUtil.color(message));
            menu.load();
            menu.set(handler.player());
            return;
        }

        if (args.length == 1 && args[0].equals("leave")) {
            EditorModeUtil.leaveEditorMode(handler.player());
            return;
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("toggleTeam")) {
                EditorColorTeamsMenu menu = (EditorColorTeamsMenu) GUIMenu.getOpenGUI(handler.player());
                GameMap map = menu.getMap();
                String team = args[1];
                boolean toAdd = !map.getData().getTeamsNames().contains(team);
                String feedbackMessage = toAdd ? "&aAdded team" : "&cRemoved team";
                if (toAdd) {
                    map.getData().getTeamsNames().add(team);
                } else {
                    map.getData().getTeamsNames().remove(team);
                }
                map.getData().save();
                handler.clicker().sendMessage(ChatUtil.color(feedbackMessage));
                menu.load();
            }
        }
    }

    private void addSubtract(Player clicker, PlayerInteractEvent event, String key, GameMap map) {
        EditMapInventoryMenu menu = (EditMapInventoryMenu) GUIMenu.getSetGUI(clicker);
        boolean add = event.getAction().name().contains("RIGHT");
        int amount = map.getData().getInt(key);
        int newAmount = add ? amount + 1 : amount - 1;
        map.getData().set(key, newAmount);
        map.getData().save();
        clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);
        menu.load();
        menu.set(clicker);
    }

}
