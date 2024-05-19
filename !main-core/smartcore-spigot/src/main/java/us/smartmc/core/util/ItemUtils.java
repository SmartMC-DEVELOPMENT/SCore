package us.smartmc.core.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemUtils {

    public static final ItemStack wand = ItemBuilder.of(Material.GOLDEN_AXE).name("Hacha mágica de regiones").get();

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
