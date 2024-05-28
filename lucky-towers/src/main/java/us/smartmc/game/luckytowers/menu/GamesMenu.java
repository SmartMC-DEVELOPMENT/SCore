package us.smartmc.game.luckytowers.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.messages.GameItems;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

            ItemBuilder builder = item(GameItems.menu_games_playMapTemplate);

            try {
                Field field = ItemBuilder.class.getDeclaredField("item");
                field.setAccessible(true);
                ItemStack item = (ItemStack) field.get(builder);
                item.setType(map.getIcon());
                field.set(builder, item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                Field field = ItemBuilder.class.getDeclaredField("nameArgs");
                field.setAccessible(true);
                field.set(builder, new Object[]{name});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                Field field = ItemBuilder.class.getDeclaredField("loreArgs");
                field.setAccessible(true);
                field.set(builder, new Object[]{getPlayingCount(map)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            set(slot, builder.get(initPlayer),
                    "playerOption playMap " + name);
            slot++;
        }
    }

    private static int getPlayingCount(GameMap map){
        GameSessionsManager manager = LuckyTowers.getManager(GameSessionsManager.class);
        return manager.getPlayingCount(map);
    }

}
