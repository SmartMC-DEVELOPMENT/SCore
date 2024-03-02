package us.smartmc.event.eventscore.util;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static ItemBuilder getFrom(ItemStack itemStack) {
        ItemBuilder builder = ItemBuilder.of(itemStack.getType())
                .amount(itemStack.getAmount());

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return builder;
        builder.name(meta.getDisplayName());
        builder.lore(meta.getLore());
        return builder;
    }

}
