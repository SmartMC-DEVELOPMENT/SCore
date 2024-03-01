
package us.smartmc.lobbymodule.menu;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.ConfigurableMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.command.ChangeVisibilityCommand;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.lobbymodule.messages.LobbyMessages;

import java.io.File;
import java.util.Arrays;

public class JoinItemMenu extends ConfigurableMenu {

    public JoinItemMenu(Player player) {
        super(player, new SpigotYmlConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus/", "join_items.yml")));
        loadInv();
    }

    private void loadInv() {
        setItem(0, Material.COMPASS, "minigames");
        set(1, ItemBuilder.of(Material.SKULL_ITEM).data(3)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjYzMDI5Y2M4MTY3ODk3ZTY1MzVhM2M1NzM0YmJhYmFmZjE4OGQwOTA1ZjlkOTM1M2FmYWM2MmEwNmRhZGY4NiJ9fX0=")
                .name("<lang.lobby.items_cosmetics_name>")
                .lore(Arrays.asList("<lang.lobby.items_cosmetics_description>")).get(player), "itemCosmetics");

        set(4, headItem("settings"), "lobbyModule settings");

        PlayerVisibility visibility = VisibilityManager.getVisibility(player);
        set(7, VisibilityManager.getVisibilityItem(visibility).get(player), "cmd changeVisibility");

        setItem(8, Material.PAPER, "lobbies");
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
        ItemStack item = LobbyMessages.getItem(material, name).get(player);
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
