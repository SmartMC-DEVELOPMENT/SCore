package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import us.smartmc.gamescore.instance.game.map.EditMapSession;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapData;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;
import us.smartmc.gamescore.menu.EditorColorTeamsMenu;

public class EditMapInvCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {

        try {
            handler.clickEvent().setCancelled(true);
        } catch (Exception ignore){}

        try {
            handler.interactEvent().setCancelled(true);
        } catch (Exception ignore){}

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
            EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
            if (manager == null) return;
            EditMapSession session = manager.get(handler.getPlayer().getUniqueId());
            String message = session.toggleEnabled() ? "&aEnabled" : "&cDisabled";
            handler.clicker().sendMessage(ChatUtil.color(message));
            menu.load();
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
