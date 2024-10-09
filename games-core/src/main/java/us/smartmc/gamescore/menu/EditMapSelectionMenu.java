
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

    private List<GameMap> getTestingMaps(int amount) {
        List<GameMap> maps = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            maps.add(new GameMap("test_" + i, MapsManager.getManager(MapsManager.class)));
        }
        return maps;
    }

    @Override
    public void load() {
        MapsManager manager = MapsManager.getManager(MapsManager.class);
        if (manager == null) return;

        List<GameMap> mapList = new ArrayList<>(getTestingMaps(120));

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
        loadControlItems();
        load();
    }

    public void loadControlItems() {
        set(size - 1, ItemBuilder.of(Material.ARROW).name("&a>").get(), "editMapSelectMenu next");
        if (page >= 2) {
            set(size - 9, ItemBuilder.of(Material.ARROW).name("&a<").get(), "editMapSelectMenu previous");
        } else {
            set(size - 9, null);
        }
    }

    public ItemStack get(GameMap map) {
        return ItemBuilder.of(Material.STONE).name(map.getName()).get();
    }

}
