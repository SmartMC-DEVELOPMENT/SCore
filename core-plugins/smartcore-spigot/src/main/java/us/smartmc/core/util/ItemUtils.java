package us.smartmc.core.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemUtils {

    public static final ItemStack wand = ItemBuilder.of(Material.GOLD_AXE).name("Hacha mágica de regiones").get();

    public static ItemStack parsePlayerItem(Player player, ItemStack item) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta.getDisplayName() != null) {
            meta.setDisplayName(ChatUtil.parse(player, meta.getDisplayName()));
        }

        if (meta.hasLore()) {
            meta.setLore(parseItemLore(player, item));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static List<String> parseItemLore(Player player, ItemStack item) {
        if (player == null) return item.getItemMeta().getLore();
        ArrayList<String> lore = new ArrayList<>();
        for (String line : item.getItemMeta().getLore()) {
            line = ChatUtil.parse(player, line);
            if (line.contains("\n")) {
                for (String l : line.split("\\n")) {
                    lore.add(ChatUtil.parse(player, "&7" + l));
                }
            } else {
                lore.add(ChatUtil.parse(player, line));
            }
        }
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return lore;
    }

    public static ItemStack skulltexture(ItemStack head, String texture) {
        SkullMeta meta = (SkullMeta) Objects.requireNonNull(head).getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField;
        try {
            profileField = Objects.requireNonNull(meta).getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
        head.setItemMeta(meta);
        return head;
    }



}
