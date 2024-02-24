package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.LobbyCosmetics;

import java.util.List;

public class CosmeticsMainMenu extends CosmeticsAddonMenu {

    private static final LobbyCosmetics addon = LobbyCosmetics.getInstance();

    public CosmeticsMainMenu(Player player) {
        super(player, "main");

        // SETUP CONFIG
        if (player == null) {
            registerCosmeticSection("hats", 12);
            registerCosmeticSection("pets", 14);
            config.save();
        }
    }

    private void registerCosmeticSection(String id, int slot) {
        registerMenuItem(id, slot);
        SyncUtil.later(() -> {
            actionManager.registerItemAction(slot, get(slot), List.of("openCosmeticSection " + id));
        }, 25);
    }

}

