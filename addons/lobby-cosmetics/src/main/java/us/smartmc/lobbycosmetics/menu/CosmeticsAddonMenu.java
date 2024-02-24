package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;

public abstract class CosmeticsAddonMenu extends CoreMenu implements ICosmeticAddonMenu {

    private static final LobbyCosmetics addon = LobbyCosmetics.getInstance();

    public CosmeticsAddonMenu(Player player, int size, String menuId) {
        super(player, size, "<lang.cosmetics_info/lobby.menu_" + menuId + "_title>");
        registerDefaults();
    }

    @Override
    public void registerDefaults() {

    }

    protected void registerMenuItem(String id, int slot) {
        String pathPrefix = "items." + id + ".";
        config.register(pathPrefix + "type", Material.SKULL_ITEM.name());
        config.register(pathPrefix + "slot", slot);
    }
}
