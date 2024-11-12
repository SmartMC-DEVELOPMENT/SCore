package us.smartmc.addon.holograms.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.util.BukkitChatUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import us.smartmc.addon.holograms.instance.hologram.HologramArmorStand;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.addon.holograms.instance.hologram.IHologram;
import us.smartmc.addon.holograms.util.IHologramAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HologramAdapter1_20 implements IHologramAdapter {

    @Override
    public void spawnHologram(Player player, IHologram hologram) {
        hologram.getLinesArmorStands().forEach(hologramArmorStand -> {
            Location loc = hologramArmorStand.getStand().getLocation();
            ArmorStand armorStand = hologramArmorStand.getStand();
            PacketContainer spawnPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
            spawnPacket.getIntegers().write(0, armorStand.getEntityId());
            spawnPacket.getUUIDs().write(0, armorStand.getUniqueId());
            spawnPacket.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
            spawnPacket.getDoubles().write(0, loc.getX());
            spawnPacket.getDoubles().write(1, loc.getY());
            spawnPacket.getDoubles().write(2, loc.getZ());
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket);
            updateHologramMetaData(player, hologramArmorStand);
        });
    }

    @Override
    public void destroyHologram(Player player, IHologram hologram) {
        hologram.getLinesArmorStands().forEach(hologramArmorStand -> {
            ArmorStand armorStand = hologramArmorStand.getStand();
            player.hideEntity(SpigotPluginsAPI.getPlugin(), armorStand);
        });
    }

    @Override
    public void updateHologramMetaData(Player player, HologramArmorStand hologramArmorStand) {
        ArmorStand armorStand = hologramArmorStand.getStand();
        PacketContainer metadataPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadataPacket.getIntegers().write(0, armorStand.getEntityId());
        List<WrappedDataValue> metadata = new ArrayList<>();

        // BUILD NAME OPTIONAL WITH PAPERCHATUTIL AND PARSED TO LEGACY MODE (me cago en dios)
        String parsedName = LegacyComponentSerializer.legacySection().serialize(BukkitChatUtil.parse(player, armorStand.getName()));
        Optional<?> name = Optional.of(WrappedChatComponent.fromChatMessage(parsedName)[0].getHandle());

        WrappedDataValue customName = new WrappedDataValue(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), name);
        WrappedDataValue customNameVisible = new WrappedDataValue(3, WrappedDataWatcher.Registry.get(Boolean.class), true);

        WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
        metadata.add(customName);
        metadata.add(customNameVisible);
        metadata.add(new WrappedDataValue(0, byteSerializer, (byte) 0x20));

        metadataPacket.getDataValueCollectionModifier().write(0, metadata);

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, metadataPacket);
    }

    @Override
    public void spawnHologramHolder(Player player, HologramHolder holder) {
        holder.forEachHologram(hologram -> {
            spawnHologram(player, hologram);
        });
    }
}