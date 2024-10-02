
package us.smartmc.gamescore.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;

public class ManageMetadataRegionMenu extends GUIMenu {

    private final CuboidRegion region;

    public ManageMetadataRegionMenu(Player player, CuboidRegion region) {
        super(player, 9 * 3, "Manage metadata " + region.getName());
        this.region = region;
        setItems();
    }

    public void setItems() {
        set(4, ItemBuilder.of(Material.NAME_TAG).name("ADD CUSTOM METADATA").get(), "addMetadata " + region.getName());

        registerMetadata(19, "DENY@PVP", Material.DIAMOND_SWORD);
        registerMetadata(20, "DENY@DAMAGE", Material.REDSTONE);
        registerMetadata(21, "DENY@BLOCK_PLACE", Material.GRASS);

        registerMetadata(23, "DENY@BLOCK_BREAK", Material.REDSTONE_BLOCK);
        registerMetadata(24, "DENY@BLOCKS", Material.STONE);
        registerMetadata(25, "DENY@DROPS", Material.HOPPER);
    }

    @Override
    public void load() {
    }

    private void registerMetadata(int slot, String id, Material material) {
        boolean enabled = region.getDefaultConfig().hasMetaData(id);

        String enabledPrefix = enabled ? "&a" : "&c";
        ItemBuilder builder = ItemBuilder.of(material)
                .name(id).lore("&7Enabled: " + enabledPrefix + enabled);
        ItemStack item = builder.get();


        if (enabled) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        set(slot, item, "toggleRegionMetadata " + region.getName() + " " + id);
    }

}
