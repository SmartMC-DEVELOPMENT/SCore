package us.smartmc.core.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Set;

public class ReloadLanguageConfigMenu extends CoreMenu {

    public ReloadLanguageConfigMenu(Player player) {
        super(player, 6*9, "Handle: Lang configs");
    }

    @Override
    public void load() {
        int slot = 0;
        Set<String> names = MultiLanguageRegistry.getClassesNames().keySet();
        for (String name : names) {
            ItemStack item = ItemBuilder.of(Material.PAPER).name(name)
                    .lore(Arrays.asList("&eDrop to reload!"))
                    .get();
            set(slot, item, "reloadLanguageConfig " + name);
            slot++;
        }
    }
}
