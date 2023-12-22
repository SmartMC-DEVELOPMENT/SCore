package us.smartmc.core.pluginsapi.spigot.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.pluginsapi.spigot.listener.ItemActionListeners;
import us.smartmc.core.pluginsapi.util.ChatUtil;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {

    private final ItemStack item;
    private ItemMeta meta;

    protected ItemBuilder(Material material) {
        item = new ItemStack(material, 1);
        meta = item.getItemMeta();
    }

    public ItemBuilder name(String name, Object... args) {
        meta.setDisplayName(ChatUtil.parse(name, args));
        updateMeta();
        return this;
    }

    public ItemBuilder lore(List<String> lore, Object... args) {
        List<String> list = new ArrayList<>();
        lore.replaceAll(l -> ChatUtil.parse(l, args));
        for (String line : lore) {
            list.addAll(Arrays.asList(line.split("\n")));
        }
        meta.setLore(list);
        updateMeta();
        return this;
    }

    public ItemBuilder parseLore(Object... args) {
        meta.getLore().replaceAll(l -> ChatUtil.parse(l, args));
        updateMeta();
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder data(int amount) {
        item.setDurability(((Number) amount).shortValue());
        return this;
    }

    public ItemBuilder skullTexture(String texture) {
        SkullMeta skullMeta = (SkullMeta) meta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField;
        try {
            profileField = Objects.requireNonNull(skullMeta).getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        meta = skullMeta;
        updateMeta();
        return this;
    }

    public ItemStack get() {
        return get((Player) null);
    }

    public ItemStack get(Player player) {
        if (player != null && item.getItemMeta().getDisplayName() != null)
            name(ChatUtil.parse(player, item.getItemMeta().getDisplayName()));

        if (player != null && item.getItemMeta().getLore() != null) {
            List<String> lore = item.getItemMeta().getLore();
            lore.replaceAll(l -> ChatUtil.parse(player, l));
            lore(lore);
        }

        String id = getID(item);
        return item;
    }

    public ItemMeta meta() {
        return meta;
    }

    private void updateMeta() {
        item.setItemMeta(meta);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public static void setup(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new ItemActionListeners(), plugin);
    }

    public static String getID(ItemStack item) {
        StringBuilder builder = new StringBuilder(item.getType().name() + item.getAmount());

        try {
            builder.append(item.getItemMeta().getDisplayName());
        } catch (Exception ignore) {
        }

        try {
            builder.append(item.getItemMeta().getLore());
        } catch (Exception e) {
        }

        if (item.getItemMeta().getLore() != null) {
            builder.append(item.getItemMeta().getLore());
        }
        return builder.toString();
    }
}
