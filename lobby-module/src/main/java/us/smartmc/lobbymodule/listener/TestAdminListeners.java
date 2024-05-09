package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.util.TexturePackUtil;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TestAdminListeners extends AddonListener implements Listener {

    private final String link = "https://smartmc.us/pack/tests_pack.zip";
    private String sha1;

    public TestAdminListeners() {
        try {
            sha1 = TexturePackUtil.calculateSHA1FromURL(link);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!isEnabled()) return;
        if (!player.hasPermission("*")) return;
        if (sha1 == null) {
            player.sendMessage("No sha1 could been readed from texturepack!");
            return;
        }
        player.setResourcePack(link, sha1);

        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            ItemStack customItem = ItemBuilder.of(Material.DIAMOND_SWORD).modelData(676).get();
            Location location = player.getLocation();
            location.add(2, 2, 0);
            ArmorStand armorStand = player.getWorld().spawn(location, ArmorStand.class);
            armorStand.customName(Component.text("MISSAWOWA"));
            armorStand.setVisible(true);
            armorStand.setItem(EquipmentSlot.CHEST, customItem);
            player.showEntity(SmartCore.getPlugin(), armorStand);
        }, 20 * 2);
    }
}
