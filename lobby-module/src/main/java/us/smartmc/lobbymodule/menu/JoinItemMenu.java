
package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.ConfigurableMenu;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.lobbymodule.messages.LobbyMessages;

import java.io.File;
import java.util.Arrays;

public class JoinItemMenu extends ConfigurableMenu {

    public JoinItemMenu(Player player) {
        super(player, new SpigotYmlConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus/", "join_items.yml")));
    }

    @Override
    public void load() {
        loadInv();
    }

    private void loadInv() {
        setItem(0, Material.COMPASS, "minigames");
        set(1, ItemBuilder.of(Material.PLAYER_HEAD).data(3)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjYzMDI5Y2M4MTY3ODk3ZTY1MzVhM2M1NzM0YmJhYmFmZjE4OGQwOTA1ZjlkOTM1M2FmYWM2MmEwNmRhZGY4NiJ9fX0=")
                .name("<lang.lobby.items_cosmetics_name>")
                .lore(Arrays.asList("<lang.lobby.items_cosmetics_description>")).get(initPlayer), "itemCosmetics");

        ItemStack headItem = headItem("settings").get(initPlayer);

        set(4, parseHeadOwnerTo(headItem, initPlayer), "lobbyModule settings");

        PlayerVisibility visibility = VisibilityManager.getVisibility(initPlayer);
        set(7, VisibilityManager.getVisibilityItem(visibility).get(initPlayer), "cmd changeVisibility");

        if (initPlayer.hasPermission("group.builder")) {
            set(12, ItemBuilder.of(Material.NETHERITE_AXE).hideFlags()
                    .name("&aTodo el mundo a buildear &e&l(1.20)")
                    .lore("&7Pues server builds, no sé").get(), "connectTo builds");
            set(14, ItemBuilder.of(Material.DIAMOND_AXE).hideFlags()
                    .name("&aTodo el mundo a buildear &e&l(LEGACY)")
                    .lore("&7Pues server builds, no sé (Aunque para versiones antiguas like 1.8 y esas cosas)").get(), "connectTo builds18");
        }

        setItem(8, Material.PAPER, "lobbies");
    }

    public ItemStack parseHeadOwnerTo(ItemStack headItem, OfflinePlayer player) {
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        skullMeta.setOwningPlayer(initPlayer);
        headItem.setItemMeta(skullMeta);
        return headItem;
    }

    public ItemBuilder headItem(String name) {
        ItemBuilder builder = ItemBuilder.of(Material.PLAYER_HEAD)
                .data((byte) 3)
                .name("<lang.lobby.items_" + name + "_name>")
                .lore(Arrays.asList("<lang.lobby.items_" + name + "_description>"));
        return builder;
    }

    public void setItem(int slot, Material material, String name) {
        ItemStack item = LobbyMessages.getItem(material, name).get(initPlayer);
        set(slot, item);
        actionManager.registerItemAction(slot, item, config.getStringList("items." + slot + ".actions"));
    }
}
