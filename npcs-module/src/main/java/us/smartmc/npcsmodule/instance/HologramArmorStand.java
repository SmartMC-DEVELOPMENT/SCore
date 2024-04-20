package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class HologramArmorStand {

    private final ArmorStand armorStand;

    public HologramArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public void spawn(Player player) {
        Location loc = armorStand.getLocation();
        PacketContainer spawnPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, armorStand.getEntityId());
        spawnPacket.getUUIDs().write(0, armorStand.getUniqueId());
        spawnPacket.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        spawnPacket.getDoubles().write(0, loc.getX());
        spawnPacket.getDoubles().write(1, loc.getY());
        spawnPacket.getDoubles().write(2, loc.getZ());

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket);

        updateMetadata(player);
    }

    public void updateMetadata(Player player) {
        PacketContainer metadataPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadataPacket.getIntegers().write(0, armorStand.getEntityId());
        List<WrappedDataValue> metadata = new ArrayList<>();
        Optional<?> name = Optional.of(WrappedChatComponent.fromChatMessage(armorStand.getName())[0].getHandle());

        WrappedDataValue customName = new WrappedDataValue(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), name);
        WrappedDataValue customNameVisible = new WrappedDataValue(3, WrappedDataWatcher.Registry.get(Boolean.class), true);

        WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
        metadata.add(customName);
        metadata.add(customNameVisible);
        metadata.add(new WrappedDataValue(0, byteSerializer, (byte) 0x20));

        metadataPacket.getDataValueCollectionModifier().write(0, metadata);

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, metadataPacket);
    }

}
