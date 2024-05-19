package us.smartmc.lobbymodule.listener;

import org.bukkit.event.Listener;

import us.smartmc.smartaddons.plugin.AddonListener;

public class TestAdminListeners extends AddonListener implements Listener {

    /*private final String link = "https://smartmc.us/pack/tests_pack.zip";
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
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setItem(EquipmentSlot.HEAD, customItem);
    }

    @EventHandler
    public void onHologramClick(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!isEnabled()) return;
        if (!player.hasPermission("*")) return;
        if (event.getRightClicked() instanceof CraftArmorStand armorStand) {
            armorStand.setSmall(false);
        }
    }

    public static void sendEntityMetadataPacket(Player player, ArmorStand armorStand) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(armorStand.getEntityId(), List.of());
        craftPlayer.getHandle().c.b(metadataPacket);
    }*/


}
