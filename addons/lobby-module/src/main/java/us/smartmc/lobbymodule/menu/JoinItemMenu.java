
package us.smartmc.lobbymodule.menu;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import us.smartmc.core.SmartCore;
import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.spigot.SpigotPluginsAPI;
import us.smartmc.core.pluginsapi.spigot.instance.SpigotAPIConfig;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.ConfigurableMenu;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.smartmc.core.pluginsapi.util.SyncUtil;
import us.smartmc.lobbymodule.LobbyModule;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class JoinItemMenu extends ConfigurableMenu {

    public JoinItemMenu(Player player) {
        super(player, new SpigotAPIConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus/", "join_items.yml")));
        loadInv();
    }

    private void loadInv() {
        setItem(0, Material.COMPASS, "minigames");

        set(4, headItem("settings"), "lobbyModule settings");

        setItem(8, Material.PAPER, "lobbies");

        if (player.hasPermission("group.builder")) {
            set(13, ItemBuilder.of(Material.YELLOW_FLOWER)
                    .name("&aConnect to Builder Server")
                    .lore(Arrays.asList("&7Click to join!"))
                    .get(player), "connectTo builds");
        }
    }

    public ItemStack headItem(String name) {
        String skullTexture = getSkullTexture(player);
        ItemBuilder builder = ItemBuilder.of(Material.SKULL_ITEM)
                .data((byte) 3)
                .name("<lang.lobby.items_" + name + "_name>")
                .lore(Arrays.asList("<lang.lobby.items_" + name + "_description>"));
        if (skullTexture != null) builder.skullTexture(skullTexture);
        return builder.get(player);
    }

    public void setItem(int slot, Material material, String name) {

        String namePath = "<lang.lobby.items_" + name + "_name>";
        String lorePath = "<lang.lobby.items_" + name + "_description>";

        ItemBuilder builder = ItemBuilder.of(material)
                .name(namePath)
                .lore(Arrays.asList(lorePath));

        ItemStack item = builder.get(player);
        set(slot, item);
        actionManager.registerItemAction(slot, item, config.getStringList("items." + slot + ".actions"));
    }


    public static String getSkullTexture(Player player) {
        GameProfile gameProfile = ((CraftPlayer) player).getProfile();
        try {
            if (gameProfile != null) {
                Property property = gameProfile.getProperties().get("textures").iterator().next();
                if (property != null) {
                    return property.getValue();
                }
            }
        } catch (Exception ignore){}
        return null;
    }
}
