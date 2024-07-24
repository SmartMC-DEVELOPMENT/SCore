package us.smartmc.addon.holograms.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramArmorStand;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.addon.holograms.util.IHologramAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class HologramAdapter1_8 implements IHologramAdapter {

    @Override
    public void spawnHologram(Player player, Hologram hologram) {



        hologram.getLinesArmorStands().forEach(hologramArmorStand -> {
            // Enviar un paquete para mostrar el nuevo ArmorStand solo al jugador
            PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(((CraftArmorStand) hologramArmorStand.getStand()).getHandle());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutSpawnEntityLiving);
            updateHologramMetaData(player, hologramArmorStand);
        });
    }

    @Override
    public void destroyHologram(Player player, Hologram hologram) {
        hologram.getLinesArmorStands().forEach(hologramArmorStand -> {
            ArmorStand armorStand = hologramArmorStand.getStand();
            PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            packetContainer.getIntegers().write(0, armorStand.getEntityId());
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        });
    }

    @Override
    public void updateHologramMetaData(Player player, HologramArmorStand hologramArmorStand) {
        if (player == null) return;
        ArmorStand stand = hologramArmorStand.getStand();

        String parsedName = ChatUtil.parse(player, hologramArmorStand.getUnformattedLine());

        DataWatcher newWatcher = cloneDataWatcher((CraftArmorStand) stand);
        newWatcher.a(2, parsedName);
        newWatcher.update(2);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(stand.getEntityId(), newWatcher, true);
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
                    if (value instanceof Map<?,?>) {
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
