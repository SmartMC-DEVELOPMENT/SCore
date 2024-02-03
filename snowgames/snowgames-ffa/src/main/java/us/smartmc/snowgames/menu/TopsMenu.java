package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.config.LanguageConfig;

import java.util.Arrays;

public class TopsMenu extends FFAMenu {

    public TopsMenu(Player player) {
        super(player, 27, LanguageConfig.getMenuTitle("tops"));
    }

    @Override
    public void load() {
        set(13, ItemBuilder.of(Material.BARRIER)
                .name("&cERROR")
                .lore(Arrays.asList("&7<lang.snowgames/ffa/main.not_available>"))
                .get(player));
    }
}
