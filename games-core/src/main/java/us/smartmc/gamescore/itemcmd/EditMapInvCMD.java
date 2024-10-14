package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.joml.Vector3i;
import us.smartmc.gamescore.adminplayer.PlayerSelectSession;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapData;
import us.smartmc.gamescore.instance.game.map.spawn.ListSpawnsHolder;
import us.smartmc.gamescore.instance.game.map.spawn.OneSpawnHolder;
import us.smartmc.gamescore.instance.game.map.spawn.RegionSpawnHolder;
import us.smartmc.gamescore.instance.game.map.spawn.SpawnType;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;
import us.smartmc.gamescore.menu.EditorColorTeamsMenu;
import us.smartmc.gamescore.util.EditorModeUtil;

import java.util.List;
import java.util.Optional;

public class EditMapInvCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {

        Player player = handler.clicker();
        boolean isLeft = false;
        GUIMenu menu = GUIMenu.getSetGUI(handler.player());



        GameMap map = switch (menu.getClass().getSimpleName()) {
            case "EditMapInventoryMenu" ->  ((EditMapInventoryMenu) menu).getMap();
            case "EditorColorTeamsMenu" -> ((EditorColorTeamsMenu) menu).getMap();
            default -> null;
        };

        GameTeam team = switch (menu.getClass().getSimpleName()) {
            case "EditMapInventoryMenu" ->  ((EditMapInventoryMenu) menu).getTeam();
            default -> null;
        };

        try {
            handler.clickEvent().setCancelled(true);
            isLeft = handler.clickEvent().isLeftClick();
        } catch (Exception ignore) {
        }

        try {
            handler.interactEvent().setCancelled(true);
            isLeft = handler.interactEvent().getAction().name().contains("LEFT");
        } catch (Exception ignore) {
        }

        // Add & subtract (Map data int values) handle:
        if (args.length == 1) {
            String key = switch (args[0]) {
                case "max" -> GameMapData.MAX_PLAYERS;
                case "min" -> GameMapData.MIN_PLAYERS;
                case "teamLimit" -> GameMapData.TEAMS_LIMIT;
                default -> null;
            };
            if (key != null) {
                addSubtract(handler.player(), isLeft, key, map);
            }

            // Paste region map
            if (args[0].equals("paste")) {
                getManager(EditMapSessionsManager.class).ifPresent(manager -> {
                    manager.loadMapAt(handler.player(), map.getName());
                });
            }

            // Save region selected as region map (SBackend)
            if (args[0].equals("save")) {
                getManager(EditMapSessionsManager.class).ifPresent(manager -> {
                    manager.saveMap(handler.player());
                });
            }

            // Open toggler of teams for the map
            if (args[0].equals("teamNames")) {
                new EditorColorTeamsMenu(handler.player(), map).open(handler.player());
            }

            if (args[0].equals("maintenance")) {
                boolean toSet = !map.isEnabled();
                map.getData().set(GameMapData.ENABLED, toSet);
                map.getData().save();
                String message = !toSet ? "&aEnabled" : "&cDisabled";
                handler.clicker().sendMessage(ChatUtil.color(message));
                reloadMenu(player);
            }

            if (args[0].equals("toggleSpawnType")) {
                SpawnType newType = map.getData().getSpawnsData().toggleSpawnType();
                map.getData().save();
                String message = "&aChanged to: &f" + newType.name();
                handler.clicker().sendMessage(ChatUtil.color(message));
            }

            if (args[0].equals("toggleTeam")) {
                GameTeam newTeam = ((EditMapInventoryMenu)menu).toggleTeam();
                String name = newTeam == null ? "null" : newTeam.getName();
                String message = "&aChanged to: &f" + name;
                handler.clicker().sendMessage(ChatUtil.color(message));
            }

            if (args[0].equals("removeLastPos")) {
                if (map.getData().getSpawnsData().getHolder() instanceof ListSpawnsHolder holder) {
                    List<Vector3i> posList = holder.getPositions(team);
                    posList.remove(posList.size() - 1);
                    holder.savePositions(team, posList);
                }
            }

            if (args[0].equals("leave")) {
                EditorModeUtil.leaveEditorMode(handler.player());
            }

            if (args[0].equals("removePos")) {
                // One
                if (map.getData().getSpawnsData().getHolder() instanceof OneSpawnHolder holder) {
                    holder.removePosition(team);
                    String teamName = team == null ? "null" : team.getName();
                    player.sendMessage(ChatUtil.color("&aRemoved position for team: &f" + teamName));
                }

                // Region
                if (map.getData().getSpawnsData().getHolder() instanceof RegionSpawnHolder holder) {
                    if (isLeft) {
                        holder.removeMin(team);
                        String teamName = team == null ? "null" : team.getName();
                        player.sendMessage(ChatUtil.color("&aRemoved min from team:&f " + teamName));
                    } else {
                        holder.removeMax(team);
                        String teamName = team == null ? "null" : team.getName();
                        player.sendMessage(ChatUtil.color("&aRemoved max from team:&f " + teamName));
                    }
                }
            }

            if (args[0].equals("addSetPosition")) {
                PlayerSelectSession selectSession = EditorModeUtil.getSelectionSession(player);
                if (selectSession == null) {
                    player.sendMessage(ChatUtil.color("&cYou don't have a current selection! (Select the map region, save and add/set relative position)"));
                }
                Vector3i relativePos = EditorModeUtil.getRelativePosition(player, player.getLocation());
                try {
                    // One
                    if (map.getData().getSpawnsData().getHolder() instanceof OneSpawnHolder holder) {
                        holder.setPosition(team, relativePos);
                        player.sendMessage(ChatUtil.color("&aPosition set/added!"));
                    }
                    // Region
                    if (map.getData().getSpawnsData().getHolder() instanceof RegionSpawnHolder holder) {
                        String message = isLeft ? "&aMin set!" : "&aMax set!";
                        if (isLeft)
                            holder.setMinRelative(team, relativePos);
                        else holder.setMaxRelative(team, relativePos);
                        player.sendMessage(ChatUtil.color(message));
                    }
                    // List
                    if (map.getData().getSpawnsData().getHolder() instanceof ListSpawnsHolder holder) {
                        holder.addPosition(team, relativePos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(ChatUtil.color("&cSomething went wrong! (Make sure you are inside the map selected region)"));
                }

            }
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("toggleTeam")) {
                String teamName = args[1];
                boolean toAdd = !map.getData().getTeamsNames().contains(teamName);
                String feedbackMessage = toAdd ? "&aAdded team" : "&cRemoved team";
                if (toAdd) {
                    map.getData().getTeamsNames().add(teamName);
                } else {
                    map.getData().getTeamsNames().remove(teamName);
                }
                map.getData().save();
                handler.clicker().sendMessage(ChatUtil.color(feedbackMessage));
                menu.load();
            }
        }

        reloadMenu(player);
    }

    private void reloadMenu(Player player) {
        GUIMenu guiMenu = GUIMenu.getSetGUI(player);
        guiMenu.load();
        guiMenu.set(player);
    }

    private void addSubtract(Player clicker, boolean left, String key, GameMap map) {
        boolean add = !left;
        int amount = map.getData().getInt(key);
        int newAmount = add ? amount + 1 : amount - 1;
        map.getData().set(key, newAmount);
        map.getData().save();
        clicker.playSound(clicker.getLocation(), Sound.CLICK, 1, 1);
    }

    private static <T extends MapManager<?, ?>> Optional<T> getManager(Class<T> type) {
        T manager = MapManager.getManager(type);
        if (manager == null) return Optional.empty();
        return Optional.of(manager);
    }

}
