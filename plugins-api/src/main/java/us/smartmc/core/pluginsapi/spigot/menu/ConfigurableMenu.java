package us.smartmc.core.pluginsapi.spigot.menu;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.pluginsapi.spigot.instance.SpigotAPIConfig;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;

import java.util.List;
import java.util.Set;

public class ConfigurableMenu extends CoreMenu {

    protected final SpigotAPIConfig config;

    public ConfigurableMenu(Player player, SpigotAPIConfig config) {
        super(player, config.getInt("size"), config.getString("title"));
        this.config = config;
        loadFromConfigPath(player, "items");
    }

    private void loadFromConfigPath(Player player, String path) {
        ConfigurationSection section = config.getSection(path);
        if (section == null) return;
        for (String sectionPath : section.getKeys(false)) {
            try {
                int slot = Integer.parseInt(sectionPath);
                String itemPath = section.getName() + "." + sectionPath;
                ItemBuilder builder = config.getItem(player, itemPath);
                ItemStack item = builder.get(player);
                if (config.contains(itemPath + ".actions")) {
                    List<String> actions = config.getStringList(itemPath + ".actions");
                    actionManager.registerItemAction(slot, item, actions);
                }
                set(slot, item);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error trying to parse slot to ConfigurableMenu (" + sectionPath + ")");
            }
        }
    }

    @Override
    public void load() {

    }
}
