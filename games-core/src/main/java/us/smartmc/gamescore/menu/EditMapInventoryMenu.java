package us.smartmc.gamescore.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.spawn.ListSpawnsHolder;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.manager.map.MapsManager;
import us.smartmc.gamescore.manager.player.PlayerRegionSelectionsManager;
import us.smartmc.gamescore.manager.team.GenericGameTeamsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class EditMapInventoryMenu extends GUIMenu {

    private final GameMap map;
    private final ItemStack[] oldContent;

    private GameTeam team;

    public EditMapInventoryMenu(Player player, String mapName) {
        super(player, 9 * 4, "Edit Map");
        MapsManager manager = MapsManager.getManager(MapsManager.class);
        this.map = manager != null ? manager.get(mapName) : null;
        this.oldContent = player.getInventory().getContents();
    }

    @Override
    public void load() {
        set(0, ItemBuilder.of(Material.SKULL_ITEM).data(3).skullTexture("ewogICJ0aW1lc3RhbXAiIDogMTcyNDU4MjA4NDYwOCwKICAicHJvZmlsZUlkIiA6ICIxNTY3ODg4YTAwYWY0ODc2YjYyNTI3YTNiOGY3YTdlYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUb3NoaW9YdHJlbWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNjNzZkZWZhN2E1M2FlNDI0ZjgzN2FhN2RmNmJhMzMwZmQ1ZWYzOGZjYTk4ZmQ0ZmEyNGFlODJkNzFmMzQ3MiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9")
                        .name("&bMax Players &f(" + map.getData().getMaxPlayers() + ")")
                        .lore(Arrays.asList("&7Right-Click to &aadd", "&7Left-Click to &csubtract"))
                        .get(),
                "editMapInv max");
        set(1, ItemBuilder.of(Material.SKULL_ITEM).data(3).skullTexture("ewogICJ0aW1lc3RhbXAiIDogMTcyODU0MjQxNzMyOSwKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDc1ZDc0OTYwNmJlYTdmZjUyYzYzYTQ4YzAwODJkYjg3N2JiZTUyMGI3NTg1MjM2Mjc5MDlmNTNhOGU1OTRjYyIKICAgIH0KICB9Cn0=")
                        .name("&bMin Players &f(" + map.getData().getMinPlayers() + ")")
                        .lore(Arrays.asList("&7Right-Click to &aadd", "&7Left-Click to &csubtract"))
                        .get(),
                "editMapInv min");
        set(3, ItemBuilder.of(Material.WOOL).data(4)
                        .name("&bTeams Limit &f(" + map.getData().getTeamsLimit() + ")")
                        .lore(Arrays.asList("&7Right-Click to &aadd", "&7Left-Click to &csubtract"))
                        .get(),
                "editMapInv teamLimit");
        set(4, ItemBuilder.of(Material.BEACON)
                        .name("&bTeams Names")
                        .lore(Arrays.asList("&7Click to open menu"))
                        .get(),
                "editMapInv teamNames");

        set(6, ItemBuilder.of(Material.PAPER)
                        .name("&bPaste map region")
                        .lore(Arrays.asList("&7Click to paste map region"))
                        .get(),
                "editMapInv paste");
        set(7, ItemBuilder.of(Material.CHEST)
                        .name("&bSave selected region")
                        .lore(Arrays.asList("&7Click to save map region"))
                        .get(),
                "editMapInv save");
        set(8, PlayerRegionSelectionsManager.wandItem);

        String maintenancePrefix = map.isEnabled() ? "&a" : "&c";
        String texture = map.isEnabled() ?
                "ewogICJ0aW1lc3RhbXAiIDogMTcyNzI2NzEwNTQ3MywKICAicHJvZmlsZUlkIiA6ICI0M2NmNWJkNjUyMDM0YzU5ODVjMDIwYWI3NDE0OGQxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJrYW1pbDQ0NSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81YWVhZjRkNDRiMzhmNGI1NmFlY2JkZGRiM2I1ZjRhOWFhNzMzZjY2NzY0YmY0YzE1NjdjNjkxOGM0YTIxMzEwIgogICAgfQogIH0KfQ==" :
                "ewogICJ0aW1lc3RhbXAiIDogMTcxOTE0NTkzNTI0MCwKICAicHJvZmlsZUlkIiA6ICI5ODY5ZmUyY2FjMjA0YmJmYjc1Y2Y1Mjk2ZTY0MDQ5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJIZW5yaXF1ZTkzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYwNDQwMGM2MWE5MjZiNWFhNDIyOGQ5ZmRjOWM1OWQxZGUyYTY4MjY4MDc1NDNkODE3NGQwODBlNTdiY2U5MDEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";

        List<String> lore = map.isEnabled() ? List.of("&7ENABLED") : List.of("&7DISABLED");

        set(13, ItemBuilder.of(Material.SKULL_ITEM).data(3).skullTexture(texture)
                .name(maintenancePrefix + "Maintenance")
                .lore(lore)
                .get(), "editMapInv maintenance");

        set(17, ItemBuilder.of(Material.BED)
                .name("&4Leave editor mode")
                .get(), "editMapInv leave");

        String spawnTypeName = map.getData().getSpawnsData().getSpawnType().name();
        set(29, ItemBuilder.of(Material.REDSTONE_WIRE)
                .name("&bSpawnType selector:&a " + spawnTypeName)
                .get(), "editMapInv toggleSpawnType");

        set(30, ItemBuilder.of(Material.STICK)
                .name("&bTeam selector: " + team.getName())
                .get(), "editMapInv toggleTeam");

        set(32, ItemBuilder.of(Material.ENDER_PORTAL_FRAME)
                .name("&bAdd/Set Position")
                .get(), "editMapInv addSetPosition");

        if (map.getData().getSpawnsData().getHolder() instanceof ListSpawnsHolder) {
            set(33, ItemBuilder.of(Material.ENDER_PORTAL_FRAME)
                    .name("&bRemove last spawn position")
                    .get(), "editMapInv removeLastPos");
        } else {
            set(33, null);
        }
    }

    public GameTeam toggleTeam() {
        List<GameTeam> teams = new ArrayList<>();
        GenericGameTeamsManager manager = MapManager.getManager(GenericGameTeamsManager.class);
        if (manager == null) return null;

        for (String name : map.getData().getTeamsNames()) {
            teams.add(manager.getGameTeam(name));
        }

        int currentIndex = (team == null ? -1 : teams.indexOf(team));
        int nextIndex = (currentIndex + 1) % (teams.size() + 1);

        team = (nextIndex == teams.size()) ? null : teams.get(nextIndex);

        return team;
    }

    public void leave(Player player) {
        player.getInventory().setContents(oldContent);
        EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
        manager.remove(player.getUniqueId());
    }

}
