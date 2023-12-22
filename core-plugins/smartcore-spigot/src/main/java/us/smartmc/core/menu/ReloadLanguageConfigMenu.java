package us.smartmc.core.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.pluginsapi.language.MultiLanguageRegistry;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.util.ChatUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class ReloadLanguageConfigMenu extends CoreMenu {

    public ReloadLanguageConfigMenu(Player player) {
        super(player, 6*9, "Handle language configs - Admin menu");
    }

    @Override
    public void load() {
        int slot = 0;
        Set<String> names = MultiLanguageRegistry.getNames();
        for (String name : names) {
            ItemStack item = ItemBuilder.of(Material.PAPER).name(name)
                    .lore(Arrays.asList("&eDrop to reload!"))
                    .get();
            set(slot, item, "msg &cEn mantenimiento manito");
            slot++;
        }
    }
}
