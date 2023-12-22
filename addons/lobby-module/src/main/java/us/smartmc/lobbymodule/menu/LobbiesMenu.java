package us.smartmc.lobbymodule.menu;

import us.smartmc.core.pluginsapi.spigot.SpigotPluginsAPI;
import us.smartmc.core.pluginsapi.spigot.instance.SpigotAPIConfig;
import us.smartmc.core.pluginsapi.spigot.menu.ConfigurableMenu;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.core.util.PluginUtils;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.util.PlayerUtil;

import java.io.File;
import java.util.Arrays;


public class LobbiesMenu extends ConfigurableMenu {

    public LobbiesMenu(Player player) {
        super(player, new SpigotAPIConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus", "lobbies.yml")));
    }

    @Override
    public void load() {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            set(slot, ItemBuilder.of(Material.BARRIER).name("<lang.lobby.items_not_available_name>").get(player));
        }
    }
}

