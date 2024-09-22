package us.smartmc.gamescore.util;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;

public class InventorySerializer {

    private static final char ITEM_SEPARATOR = '\n';

    public static String serializeContents(ItemStack[] contents) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(contents).forEach(itemStack -> {
            try {
                builder.append(ItemSerializer.serializeItemStack(itemStack));
                builder.append(ITEM_SEPARATOR);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return builder.toString();
    }

    public static ItemStack[] deserializeContents(String contents) {
        String[] lines = contents.split(String.valueOf(ITEM_SEPARATOR));
        ItemStack[] itemStacks = new ItemStack[lines.length];

        for (int index = 0; index < itemStacks.length; index++) {
            String serializedBase64Item = lines[index];
            try {
                itemStacks[index] = ItemSerializer.deserializeItemStack(serializedBase64Item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return itemStacks;
    }

}
