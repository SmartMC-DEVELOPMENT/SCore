package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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
import java.util.List;

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

        ItemStack customItem = ItemBuilder.of(Material.DIAMOND_SWORD).modelData(676).get();
        ArmorStand armorStand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCanPickupItems(false);
        armorStand.customName(Component.text("¡WELCOME!"));
        armorStand.setCustomNameVisible(true);
        armorStand.setItem(EquipmentSlot.HEAD, customItem);
    }

    public static void sendEntityMetadataPacket(Player player, ArmorStand armorStand) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(armorStand.getEntityId(), List.of());
        craftPlayer.getHandle().c.b(metadataPacket);
    }

}
