package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.ConfigurableMenu;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.helper.CosmeticLanguageInfo;

import java.io.File;
import java.util.List;

public abstract class CosmeticsAddonMenu extends ConfigurableMenu implements ICosmeticAddonMenu {

    private static final LobbyCosmetics addon = LobbyCosmetics.getInstance();

    public CosmeticsAddonMenu(Player player, String menuId) {
        super(player, new SpigotYmlConfig(new File(addon.getDataFolder() + "/menu", menuId + ".yml")), "<lang.cosmetics_info/lobby.menu_" + menuId + ".title>");
        registerDefaults();
        config.save();
    }

    @Override
    public void registerDefaults() {

    }

    protected void registerMenuItem(String id, int slot) {
        String pathPrefix = "items." + id + ".";
        config.register(pathPrefix + "type", Material.SKULL_ITEM.name());
        config.register(pathPrefix + "slot", slot);
    }

    @Override
    protected void loadItemsFromSpecificPath(Player player, String path) {
        ConfigurationSection section = config.getSection(path);
        if (section == null) return;
        for (String sectionName : section.getKeys(false)) {
            try {
                String itemPath = section.getName() + "." + sectionName;
                int slot = config.getInt(itemPath + ".slot");
                Language language = PlayerLanguages.get(player.getUniqueId());
                CosmeticLanguageInfo info = new CosmeticLanguageInfo(sectionName, language, "cosmetics_info/lobby");
                ItemBuilder builder = config.getItem(player, itemPath).name(info.getName()).lore(info.getDescription());
                ItemStack item = builder.get(player);
                if (config.contains(itemPath + ".actions")) {
                    List<String> actions = config.getStringList(itemPath + ".actions");
                    actionManager.registerItemAction(slot, item, actions);
                }
                set(slot, item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
