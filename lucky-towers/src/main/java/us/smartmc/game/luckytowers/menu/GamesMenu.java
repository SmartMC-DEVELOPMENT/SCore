package us.smartmc.game.luckytowers.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class GamesMenu extends GameMenu {

    public GamesMenu(Player player) {
        super(player, 9 * 4, GameMessages.menu_games_title);
    }

    @Override
    public void load() {
        GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);

        int slot = 0;

        for (GameMap map : mapManager.values()) {
            String name = map.getName();
            set(slot, ItemBuilder.of(Material.ACACIA_STAIRS)
                            .name("<color:#db03fc>" + name)
                            .lore("Click to play! Yo que sé").get(),
                    "playerOption playMap " + name);
            slot++;
        }
    }

}
