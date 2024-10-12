package us.smartmc.gamescore.instance.game.map;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.manager.map.MapsManager;

import java.util.ArrayList;
import java.util.Set;

@Getter
public class EditMapSession {

    private final Player player;
    private final GameMap map;

    private final boolean canFly;
    private final GameMode gamemode;
    private final Location location;
    private final ItemStack[] invContent;

    public EditMapSession(Player player, String mapName) {
        this.player = player;
        MapsManager manager = MapsManager.getManager(MapsManager.class);
        if (manager != null) this.map = manager.get(mapName);
        else this.map = null;

        canFly = player.getAllowFlight();
        invContent = player.getInventory().getContents();
        gamemode = player.getGameMode();
        location = player.getLocation();

        player.setAllowFlight(true);
        player.setFlying(true);

        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealthScale(20);
    }

    public void leave() {
        if (!canFly && !player.getGameMode().equals(GameMode.CREATIVE)) {
            player.setAllowFlight(false);
        }

        player.setGameMode(gamemode);
        player.getInventory().clear();
        player.getInventory().setContents(invContent);

        player.teleport(location);
    }

    public void setTeamsNames(Set<String> teamNames) {
        setAndSave(GameMapData.TEAMS_LIMIT, new ArrayList<>(teamNames));
    }

    public void setTeamsLimit(int amount) {
        setAndSave(GameMapData.TEAMS_LIMIT, amount);
    }

    public void setMax(int amount) {
        setAndSave(GameMapData.MAX_PLAYERS, amount);
    }

    public void setMin(int amount) {
        setAndSave(GameMapData.MIN_PLAYERS, amount);
    }

    public boolean toggleEnabled() {
        setAndSave(GameMapData.ENABLED, !map.isEnabled());
        return map.isEnabled();
    }

    private void setAndSave(String key, Object value) {
        map.getData().set(key, value);
        map.getData().save();
    }

    public static EditMapSession of(Player player) {
        EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
        if (manager == null) return null;
        return manager.of(player);
    }
}
