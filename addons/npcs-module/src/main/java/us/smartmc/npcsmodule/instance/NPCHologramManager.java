package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LanguageUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.smartmc.core.SmartCore;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.*;


public class NPCHologramManager extends AddonListener implements Listener {

    private final CustomNPC npc;
    private final HashMap<Integer, CraftArmorStand> stands = new HashMap<>();
    private final HashMap<Integer, String> names = new HashMap<>();
    private final Set<Player> viewers = new HashSet<>();

    private final int updateTaskID;

    private final Map<Language, List<String>> constantLinesMap = new HashMap<>();
    private final Map<Language, Integer> lastKnownLines = new HashMap<>();

    public NPCHologramManager(CustomNPC npc) {
        this.npc = npc;
        updateTaskID = setupUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, SmartCore.getPlugin());
    }

    private int setupUpdateTask() {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            if (viewers.isEmpty()) return;

            constantLinesMap.clear();
            // Register only changed lines in the map below
            for (Player player : viewers) {
                if (constantLinesMap.keySet().size() == Language.values().length) break;
                Language language = PlayerLanguages.get(player.getUniqueId());
                if (constantLinesMap.containsKey(language)) continue;
                List<String> linesList = getLines(language);

                // Calcula el hash de las líneas actuales
                int currentLinesHash = linesList.hashCode();

                // Compara el hash actual con el último hash conocido
                Integer lastKnownLinesHash = lastKnownLines.containsKey(language) ? lastKnownLines.get(language).hashCode() : null;
                if (lastKnownLinesHash != null && currentLinesHash == lastKnownLinesHash) continue;

                constantLinesMap.put(language, linesList);
                // Actualiza el hash en lastKnownLines
                lastKnownLines.put(language, currentLinesHash); // Considera almacenar el hash directamente para mejorar la eficiencia
            }

            // Update holograms to players if changed
            for (Player player : viewers) {
                UUID uuid = player.getUniqueId();
                Language language = PlayerLanguages.get(uuid);
                if (!constantLinesMap.containsKey(language)) continue;
                updateHolograms(player, constantLinesMap.get(language));
            }
        }, 0, 20 * 3);
    }

    public void cancelUpdateTask() {
        Bukkit.getScheduler().cancelTask(updateTaskID);
    }

    public void setupStands() {
        List<String> lines = getLines();
        if (lines == null) return;
        if (lines.isEmpty()) return;

        Location location = npc.getLocation();
        location.add(0, 0.92, 0);

        if (location.getWorld() == null) return;

        ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();

        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            CraftArmorStand stand = (CraftArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setSmall(true);
            stands.put(i, stand);
            names.put(stand.getEntityId(), line);
            location.add(0, 0.3, 0);
        }
    }

    public void createHologram(Player player) {
        List<String> originalLines = getLines();
        if (originalLines == null) return;
        List<String> lines = new ArrayList<>(originalLines);
        if (lines.isEmpty()) return;
        for (int i = lines.size() - 1; i >= 0; i--) {
            CraftArmorStand stand = stands.get(i);
            spawnVisibleArmorStand(player, stand);
        }
    }

    private List<String> getLines(Language language) {
        List<String> list = new ArrayList<>();
        for (String line : getLines()) {
            String languageParsedLine = LanguageUtil.parse(language, line);
            list.add(ChatUtil.parse(languageParsedLine));
        }
        return list;
    }

    public void updateHolograms(Player player, List<String> names) {
        for (int i = 0; i < stands.size(); i++) {
            ArmorStand stand = (ArmorStand) stands.get(i);
            String name = names.get(i);
            String parsedName = ChatUtil.parse(player, name);

            // Obtener el dataWatcher del armor stand
            WrappedDataWatcher dataWatcher = WrappedDataWatcher.getEntityWatcher(stand).deepClone();

            // Modificar el dataWatcher para actualizar el nombre
            dataWatcher.setObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), parsedName);

            // Crear un nuevo paquete para enviar los metadatos actualizados al cliente
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, stand.getEntityId());
            packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

            // Enviar el paquete al cliente
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        }
    }

    public void spawnVisibleArmorStand(Player player, CraftArmorStand originalArmorStand) {
        Location location = originalArmorStand.getLocation();
        EntityArmorStand visibleArmorStand = (EntityArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        String name = originalArmorStand.getName();
        // Obtener el EntityLiving del nuevo ArmorStand
        originalArmorStand.setCustomName(ChatUtil.parse(player, name));

        // Enviar un paquete para mostrar el nuevo ArmorStand solo al visibleArmorStandEntity
        PacketPlayOutSpawnEntity packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntity(visibleArmorStand);
        ((EntityPlayer) player).connection.send(packetPlayOutSpawnEntityLiving);

        updateHolograms(player, getLines(PlayerLanguages.get(player.getUniqueId())));
        viewers.add(player);
    }

    public void removeViewer(Player player) {
        viewers.remove(player);
    }

    public List<String> getLines() {
        return npc.getLines();
    }
}
