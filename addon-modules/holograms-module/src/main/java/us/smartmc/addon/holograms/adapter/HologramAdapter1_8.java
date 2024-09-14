package us.smartmc.addon.holograms.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import us.smartmc.addon.holograms.instance.hologram.HologramArmorStand;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.addon.holograms.instance.hologram.IHologram;
import us.smartmc.addon.holograms.util.IHologramAdapter;

import java.lang.reflect.Field;
import java.util.*;

public class HologramAdapter1_8 implements IHologramAdapter {

    public void spawnHologram(Player player, IHologram hologram) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        hologram.getLinesArmorStands().forEach(hologramArmorStand -> {
            ArmorStand armorStand = hologramArmorStand.getStand();
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(ChatUtil.parse(player, hologramArmorStand.getUnformattedLine()));
            armorStand.setGravity(false);

            PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(((CraftArmorStand) armorStand).getHandle());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnPacket);

            // Actualizar la metadata del holograma si es necesario
            updateHologramMetaData(player, hologramArmorStand);
        });
    }

    @Override
    public void destroyHologram(Player player, IHologram hologram) {
        hologram.getLinesArmorStands().forEach(hologramArmorStand -> {
            ArmorStand armorStand = hologramArmorStand.getStand();
            hologramArmorStand.getStand().eject();
            PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            packetContainer.getIntegers().write(0, armorStand.getEntityId());
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        });
    }

    @Override
    public void updateHologramMetaData(Player player, HologramArmorStand hologramArmorStand) {
        EntityArmorStand nmsArmorStand = ((CraftArmorStand) hologramArmorStand.getStand()).getHandle();

        // Crear el DataWatcher y establecer el nuevo nombre
        DataWatcher dataWatcher = new DataWatcher(nmsArmorStand);
        dataWatcher.a(2, ChatUtil.parse(player, hologramArmorStand.getUnformattedLine()));  // 2 es el índice para el nombre en DataWatcher
        dataWatcher.a(3, (byte) 1);

        // Crear el paquete de metadata y enviarlo al jugador
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(nmsArmorStand.getId(), dataWatcher, true);

        // Enviar el paquete al jugador
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void spawnHologramHolder(Player player, HologramHolder holder) {
        holder.forEachHologram(hologram -> {
            spawnHologram(player, hologram);
        });
    }

    public static DataWatcher cloneDataWatcher(CraftArmorStand entity) {

        DataWatcher originalWatcher = entity.getHandle().getDataWatcher();
        DataWatcher newWatcher = new DataWatcher(entity.getHandle()); // Create a new DataWatcher instance

        HashMap<Integer, DataWatcher.WatchableObject> originalData = new HashMap<>();

        try {
            for (Field field : DataWatcher.class.getDeclaredFields()) {
                if (field.getName().equals("b")) {
                    field.setAccessible(true);
                    Object value = field.get(originalWatcher);
                    if (value instanceof Map<?, ?>) {
                        originalData.putAll((Map<? extends Integer, ? extends DataWatcher.WatchableObject>) value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (int num : originalData.keySet()) {
            newWatcher.watch(num, originalData.get(num));
        }
        return newWatcher;
    }

}
