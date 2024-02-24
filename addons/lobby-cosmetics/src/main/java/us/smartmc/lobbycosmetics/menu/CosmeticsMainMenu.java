package us.smartmc.lobbycosmetics.menu;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;

public class CosmeticsMainMenu extends CosmeticsAddonMenu {

    public CosmeticsMainMenu(Player player) {
        super(player, 27, "main");
    }

    @Override
    public void registerDefaults() {
        registerSection(12, CosmeticType.HATS);
        registerSection(14, CosmeticType.PETS);
    }
}

