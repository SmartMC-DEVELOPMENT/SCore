package us.smartmc.gamescore.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.team.ColorGameTeamColor;

import java.util.List;

public class EditorColorTeamsMenu extends GUIMenu {

    @Getter
    private final GameMap map;

    public EditorColorTeamsMenu(Player player, GameMap map) {
        super(player, 18, "Teams");
        this.map = map;
    }

    @Override
    public void load() {
        int slot = 0;
        for (ColorGameTeamColor team : ColorGameTeamColor.values()) {
            setTeam(slot, team);
            slot++;
        }
    }

    public void setTeam(int slot, ColorGameTeamColor team) {
        String name = team.getName();
        boolean isAdded = map.getData().getTeamsNames().contains(name);
        ItemBuilder builder = ItemBuilder.of(Material.WOOL).name("&" + team.getCode() + name);
        List<String> lore = isAdded ? List.of("&cClick to disable") : List.of("&aClick to enable");

        if (isAdded) {
            builder.meta().addEnchant(Enchantment.DURABILITY, 1, true);
        }

        builder.lore(lore);
        set(slot, builder.get(), "editMapInv toggleTeam " + name);
    }

}
