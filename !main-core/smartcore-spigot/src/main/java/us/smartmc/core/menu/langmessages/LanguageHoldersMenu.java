package us.smartmc.core.menu.langmessages;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Set;

public class LanguageHoldersMenu extends CoreMenu {

    public LanguageHoldersMenu(Player player) {
        super(player, 9 * 6, "Holders");
    }

    @Override
    public void load() {
        int slot = 0;
        Set<String> names = MultiLanguageRegistry.getClassesNames().keySet();
        for (String name : names) {
            ItemStack item = ItemBuilder.of(Material.PAPER).name("&b" + name)
                    .lore(Arrays.asList("&eClick to open settings"))
                    .get();
            set(slot, item, "langMessages openSettings " + name);
            slot++;
        }
    }
}
