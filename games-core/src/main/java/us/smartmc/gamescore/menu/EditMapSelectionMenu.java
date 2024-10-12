
package us.smartmc.gamescore.menu;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.manager.map.MapsManager;

import java.util.*;

public class EditMapSelectionMenu extends GUIMenu {

    private final int endIndex;

    @Getter
    private int page = 1;

    public EditMapSelectionMenu(Player player) {
        super(player, 9 * 6, "Select map to edit");
        endIndex = size - 9;
        loadControlItems();
    }


    @Override
    public void load() {
        MapsManager manager = MapsManager.getManager(MapsManager.class);
        if (manager == null) return;

        List<GameMap> mapList = new ArrayList<>(manager.values());

        int listIndex = endIndex * page - endIndex;

        for (int i = 0; i < endIndex; i++) {
            if (listIndex >= mapList.size()) continue;
            GameMap map = mapList.get(listIndex);
            set(i, get(map), "startEditSession " + map.getName());
            listIndex++;
        }
    }

    public void goToPage(int page) {
        this.page = page;
        inventory.clear();
        loadControlItems();
        load();
    }

    public void loadControlItems() {
        int maxPage = (int) Math.ceil((double) getMapsSize() / (size - 9)) - 1;
        if (page > maxPage) {
            set(size - 1, null);
        } else {
            set(size - 1, ItemBuilder.of(Material.ARROW).name("&a>").get(), "editMapSelectMenu next");
        }

        if (page >= 2) {
            set(size - 9, ItemBuilder.of(Material.ARROW).name("&a<").get(), "editMapSelectMenu previous");
        } else {
            set(size - 9, null);
        }
    }

    public ItemStack get(GameMap map) {
        return ItemBuilder.of(Material.STONE).name(map.getName()).get();
    }

    public int getMapsSize() {
        return Objects.requireNonNull(MapsManager.getManager(MapsManager.class)).size();
    }

}
