package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.manager.TopsManager;
import us.smartmc.snowgames.messages.PluginMessages;
import us.smartmc.snowgames.object.MongoCollectionTop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopsMenu extends FFAMenu {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();
    private static final TopsManager topsManager = plugin.getTopsManager();

    public TopsMenu(Player player) {
        super(player, 27, LanguageConfig.getMenuTitle("tops"));
    }

    @Override
    public void load() {
        /*set(13, ItemBuilder.of(Material.BARRIER)
                .name("&cERROR")
                .lore(Arrays.asList("&7<lang.snowgames/ffa/main.not_available>"))
                .get(player));*/
        set(11, Material.DIAMOND_SWORD, "kills");
        set(13, Material.SKULL_ITEM, "deaths");
        set(15, Material.FISHING_ROD, "max_kill_streak");
    }

    private void set(int slot, Material material, String name) {
        String topName = LanguagesHandler.get(corePlayer.getLanguage()).get(PluginMessages.NAME)
                .get("top_" + name + "_name", "{TOP " + name + " NAME}");
        set(slot, ItemBuilder.of(material).name("&b&l" + topName)
                .lore(getTopLore(name)).hideFlags().get(player));
    }

    private List<String> getTopLore(String topName) {
        MongoCollectionTop top = topsManager.get(topName);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < top.getRankingAmountToGet(); i++) {
            Document document = topsManager.getTop(topName, i);
            if (document == null) break;
            String name = document.get("name", "none");
            long value = document.get("value", 0);
            list.add("&7" + name + " &8➤ &e" + value);
        }
        list.addAll(
                getListByStrings(" ",
                        ChatUtil.parse(player, "<lang." + PluginMessages.NAME+ ".tops_your_position>",
                                top.getPlayerRank(player.getUniqueId().toString())))
        );

        return list;
    }

    private static List<String> getListByStrings(String... strings) {
        return Arrays.asList(strings);
    }

}
