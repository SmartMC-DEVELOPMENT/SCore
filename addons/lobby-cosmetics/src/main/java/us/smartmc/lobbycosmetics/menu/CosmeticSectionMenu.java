package us.smartmc.lobbycosmetics.menu;

import org.bukkit.entity.Player;

public class CosmeticSectionMenu extends CosmeticsAddonMenu {

    private final String id;

    public CosmeticSectionMenu(Player player, String id) {
        super(player, "section_" + id);
        this.id = id;
    }

    @Override
    public void registerDefaults() {
        // TODO: DO COOL STUFF HERE :D
    }
}
