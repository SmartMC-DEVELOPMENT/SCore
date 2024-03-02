package us.smartmc.event.eventscore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class EventCoreMenu extends CoreMenu {

    protected EventCoreMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    protected void set(int slot, Material material, String name, String description, String command) {
        List<String> descriptionLines = listOfString(description);
        descriptionLines.replaceAll(s -> ChatUtil.color("&7" + s));
        ItemBuilder builder = ItemBuilder.of(material).name(name).lore(descriptionLines);
        set(slot, builder.get());
    }

    private static List<String> listOfString(String str) {
        return Arrays.asList(str.split("\n"));
    }

}
